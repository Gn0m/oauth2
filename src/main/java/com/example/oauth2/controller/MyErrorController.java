package com.example.oauth2.controller;

import com.example.oauth2.exception.Error;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MyErrorController implements ErrorController {

    private final Map<Integer, String> errors;

    public MyErrorController() {
        //для прикола
        this.errors = new HashMap<>();
        errors.put(403, "Доступ с Вашей ролью запрещён, обратитесь к администратору ресурса.");
        errors.put(404, "Данные согласно вашего запроса не найдены.");
        errors.put(400, "Ошибка в запросе.");
        errors.put(500, "Сервер недоступен");
    }

    @RequestMapping("/error")
    public ResponseEntity<Error> handler(HttpServletRequest request) {
        int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = errors.get(status);
        return new ResponseEntity<>(new Error(status, errorMessage),
                HttpStatus.valueOf(status));
    }
}
