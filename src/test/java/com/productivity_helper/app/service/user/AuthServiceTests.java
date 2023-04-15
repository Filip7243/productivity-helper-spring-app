package com.productivity_helper.app.service.user;

import com.productivity_helper.app.controller.user.LoginRequest;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.security.jwt.JwtResponse;
import com.productivity_helper.app.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private AppUserService userService;
    @Mock
    private JwtUtils jwtUtils;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        this.authService = new AuthService(authManager, userService, jwtUtils);
    }

    @Test
    public void checkMocks() {
        assertThat(authManager).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(jwtUtils).isNotNull();
    }

    @Test
    public void canSignIn() {
        String any = anyString();

        when(userService.loadUserByUsername(any)).thenReturn(new AppUser());

        JwtResponse jwtResponse = authService.signIn(new LoginRequest(any, any));

        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.claims().size()).isEqualTo(0); // user is with no authorities
    }

    @Test
    public void canGetJwtResponse() {
        String any = anyString();
        AppUser mockUser = new AppUser();

        when(userService.loadUserByUsername(any)).thenReturn(mockUser);
        when(jwtUtils.generateToken(mockUser)).thenReturn("mockToken");
        when(jwtUtils.generateRefreshToken(mockUser)).thenReturn("mockToken");

        JwtResponse jwtResponse = authService.signIn(new LoginRequest(any, any));

        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.newToken()).isNotNull().isEqualTo("mockToken");
        assertThat(jwtResponse.refreshToken()).isNotNull().isEqualTo("mockToken");
        assertThat(jwtResponse.claims().size()).isEqualTo(0); // user is with no authorities
    }
}