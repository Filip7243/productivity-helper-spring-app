package com.productivity_helper.app.service.user;

import com.productivity_helper.app.controller.user.LoginRequest;
import com.productivity_helper.app.controller.user.RegisterRequest;
import com.productivity_helper.app.security.jwt.JwtResponse;
import com.productivity_helper.app.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final AppUserService userService;
    private final JwtUtils jwtUtils;

    public JwtResponse signIn(LoginRequest loginRequest) {
        String username = loginRequest.username();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                loginRequest.password()
        ));

        return getJwtResponse(username);
    }

    public String signUp(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    private JwtResponse getJwtResponse(String username) {
        UserDetails user = userService.loadUserByUsername(username);
        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        List<String> authorities = user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();

        return new JwtResponse(
                username, token, refreshToken, authorities
        );
    }
}
