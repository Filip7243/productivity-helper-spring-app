package com.productivity_helper.app.controller.user;

import com.productivity_helper.app.security.jwt.JwtResponse;
import com.productivity_helper.app.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signIn")
    public ResponseEntity<JwtResponse> sigIn(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> sigUp(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.signUp(registerRequest));
    }
}
