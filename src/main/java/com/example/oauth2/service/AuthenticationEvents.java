package com.example.oauth2.service;

import com.example.oauth2.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class AuthenticationEvents {


    private final UserAttemptsService attemptsService;


    public AuthenticationEvents(UserAttemptsService attemptsService) {
        this.attemptsService = attemptsService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        if (success.getAuthentication().getPrincipal() instanceof DefaultOAuth2User oAuth2UserAuthority) {
            Map<String, Object> userAttributes = oAuth2UserAuthority.getAttributes();
            String email = (String) userAttributes.get("email");
            log.info("User " + email + " loggining, time: " + new Date());
        } else {
            CustomUserDetails customUserDetails = (CustomUserDetails) success.getAuthentication().getPrincipal();
            String email = customUserDetails.getUsername();
            log.info("User " + email + " loggining, time: " + new Date());
        }
    }


    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        Authentication authentication = failures.getAuthentication();
        String email = (String) authentication.getPrincipal();

        attemptsService.updateFailAttempts(email);

    }
    @EventListener
    public void onLogout(LogoutSuccessEvent logoutSuccessEvent){
        System.out.println(logoutSuccessEvent.getAuthentication().getPrincipal());
    }
}
