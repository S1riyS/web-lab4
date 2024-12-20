package ru.s1riys.lab4.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.s1riys.lab4.domain.dto.SignUpRequest;
import ru.s1riys.lab4.service.AuthService;
import ru.s1riys.lab4.domain.dto.SignInRequest;
import ru.s1riys.lab4.domain.dto.JwtAuthResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok().body(authService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok().body(authService.signIn(request));
    }
}
