package com.example.oauth2.config;

import com.example.oauth2.entity.User;
import com.example.oauth2.enums.Role;
import com.example.oauth2.repository.UserRepository;
import com.example.oauth2.service.PasswordGeneratorService;
import com.example.oauth2.service.UserAttemptsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final PasswordGeneratorService passwordgenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserAttemptsService attemptsService;

    public SecurityConfig(UserRepository userRepository, PasswordGeneratorService passwordgenerator, PasswordEncoder passwordEncoder, UserAttemptsService attemptsService) {
        this.userRepository = userRepository;
        this.passwordgenerator = passwordgenerator;
        this.passwordEncoder = passwordEncoder;
        this.attemptsService = attemptsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/user/create", "/login", "/css/**",
                                        "/js/**", "/images/**", "/error").permitAll()
                                .requestMatchers("/user/delete/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/home/admin").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE).hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/", true))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(userAuthoritiesMapper())))
                .build();
    }


    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            authorities.forEach(authority -> {
                if (authority instanceof OAuth2UserAuthority oAuth2UserAuthority) {
                    Map<String, Object> userAttributes = oAuth2UserAuthority.getAttributes();
                    String email = (String) userAttributes.get("email");
                    User user = userRepository.findByUserName(email).orElseGet(() -> {
                        User createUser = User.builder()
                                .userName(email)
                                .firstName((String) userAttributes.get("name"))
                                .lastName("")
                                .accountNonLocked(true)
                                .password(passwordEncoder.encode(
                                        passwordgenerator.generatePassword()))
                                .roles(new HashSet<>())
                                .build();
                        createUser.getRoles().add(Role.USER);
                        return createUser;
                    });
                    userRepository.save(user);
                    attemptsService.create(email);
                    attemptsService.resetFailAttempts(email);
                }
            });
            return authorities;
        };
    }
}
