package com.productivity_helper.app.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.productivity_helper.app.model.user.AppUserRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private String secret;
    private static final Set<String> blockedTokens = new HashSet<>();
    private final UserDetailsService userService;

    public String generateToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        log.info("Creating access token");
        return JWT.create()
                .withSubject(user.getUsername()) // passing email to token
                .withClaim("authorities",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority).toList()) // all user's roles
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 1000)) // expiration time 24h from now
                .sign(algorithm);
    }

    public String generateRefreshToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        log.info("Creating refresh token");
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(secret)
                .withExpiresAt(new Date(System.currentTimeMillis() + 48 * 60 * 1000))
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedToken = decodeToken(token);
        return decodedToken.getSubject();
    }

    public Map<String, Claim> getClaimsFromToken(String token) {
        DecodedJWT decodedToken = decodeToken(token);
        return decodedToken.getClaims();
    }

    public DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        String bearer = "Bearer ";
        if (token.startsWith(bearer) && token.contains(bearer) && !isTokenBlackListed(token)) {
            return token.substring(bearer.length());
        }

        log.info("No token in header!");
        return null;
    }

    public JwtResponse refreshJwt(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        DecodedJWT decodedToken = decodeToken(token);
        String username = decodedToken.getSubject();
        List<String> claims = getClaimsFromToken(token).get("authorities")
                .asList(AppUserRole.class).stream()
                .map(AppUserRole::getName)
                .toList();
        UserDetails user = userService.loadUserByUsername(username);

        addTokenToBlackList(token);

        if (token != null && isTokenBlackListed(token)) {
            String newToken = generateToken(user);

            log.info("Creating refresh token");
            return new JwtResponse(
                    username,
                    newToken,
                    token,
                    claims
            );
        }

        log.info("Token has not been refreshed!");
        return null;
    }

    public boolean isTokenValid(String token) {
        return !isTokenBlackListed(token) && !isTokenExpired(token);
    }

    public void addTokenToBlackList(String token) {
        blockedTokens.add(token);
        log.info("Token added to black list");
    }

    public boolean isTokenBlackListed(String token) {
        return blockedTokens.contains(token);
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT decodedToken = decodeToken(token);
        return decodedToken.getExpiresAt().before(new Date());
    }

    public Set<String> getBlockedTokens() {
        return Set.copyOf(blockedTokens);
    }

    public String getSecret() {
        return this.secret;
    }

    @Value("${jwt.token.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
