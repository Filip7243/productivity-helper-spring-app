package com.productivity_helper.app.security.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.model.user.AppUserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private UserDetailsService userService;

    private JwtUtils jwtUtils;
    private AppUser user;


    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(userService);
        if (jwtUtils.getSecret() == null) {
            jwtUtils.setSecret("testSecret");
        }

        user = new AppUser();
        user.setEmail("user@mail.com");
        AppUserRole admin = new AppUserRole("ADMIN");
        user.addRoleToUser(admin);
    }

    private String generateToken() {
        return jwtUtils.generateToken(user);
    }

    @Test
    public void canGenerateTokenAndRefreshToken() {
        String token = jwtUtils.generateToken(new AppUser());
        String refreshToken = jwtUtils.generateRefreshToken(new AppUser());

        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThan(0);
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.length()).isGreaterThan(0);
    }

    @Test
    public void canDecodeJwt() {
        String token = generateToken();
        DecodedJWT decodedToken = jwtUtils.decodeToken(token);

        assertThat(decodedToken).isNotNull();
        assertThat(decodedToken.getSubject()).isEqualTo(user.getUsername());
        assertThat(decodedToken.getToken()).isEqualTo(token);
        assertThat(decodedToken.getClaims().get("authorities")).isNotNull();
    }

    @Test
    public void canGetUsernameFromToken() {
        String token = generateToken();
        String username = jwtUtils.getUsernameFromToken(token);

        assertThat(username).isNotNull().isNotBlank().isEqualTo(user.getUsername());
    }

    @Test
    public void canGetClaimsFromToken() {
        String token = generateToken();

        Map<String, Claim> claims = jwtUtils.getClaimsFromToken(token);
        List<AppUserRole> authorities = claims.get("authorities").asList(AppUserRole.class);

        assertThat(authorities).isNotNull();
        assertThat(authorities).isInstanceOf(List.class);
        assertThat(authorities.size()).isEqualTo(1);
        assertThat(authorities.get(0)).isInstanceOf(AppUserRole.class);
        assertThat(authorities.get(0).getName()).isEqualToIgnoringCase("ADMIN");
    }

    @Test
    public void canRefreshToken() {
        String token = generateToken();
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "Bearer " + token);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(AUTHORIZATION)).thenReturn(headers.get(AUTHORIZATION));
        when(userService.loadUserByUsername(anyString())).thenReturn(new AppUser());
        JwtResponse refreshedToken = jwtUtils.refreshJwt(request);

        assertThat(refreshedToken.username()).isEqualTo("user@mail.com");
        assertThat(jwtUtils.getBlockedTokens().size()).isEqualTo(1);
    }

    @Test
    public void throwsNullPointerExceptionWhenRefreshNullToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> jwtUtils.refreshJwt(request));

        assertThat(ex.getMessage())
                .isEqualTo("Cannot invoke \"String.startsWith(String)\" because \"token\" is null");
    }



    @Test
    public void canNotGetTokenFromHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "first second third");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(AUTHORIZATION)).thenReturn(headers.get(AUTHORIZATION));
        String token = jwtUtils.getTokenFromHeader(request);

        assertThat(token).isNull();
    }

    @Test
    public void canNotAddTokenToBlackList() {
        String token = generateToken();

        jwtUtils.addTokenToBlackList(token);
        jwtUtils.addTokenToBlackList(token);

        assertThat(jwtUtils.getBlockedTokens().size()).isEqualTo(1);
    }

    @Test
    public void canCheckIfTokenExpired() {
        String token = generateToken();

        boolean tokenExpired = jwtUtils.isTokenExpired(token);

        assertThat(tokenExpired).isFalse();
    }

    @Test
    public void canCheckIfTokenValid() {
        AppUser user = new AppUser();

        String token = jwtUtils.generateToken(user);
        boolean isValid = jwtUtils.isTokenValid(token);

        assertThat(isValid).isTrue();
    }
}