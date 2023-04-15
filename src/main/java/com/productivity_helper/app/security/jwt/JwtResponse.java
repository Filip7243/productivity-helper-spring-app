package com.productivity_helper.app.security.jwt;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public record JwtResponse(String username, String newToken, String refreshToken, List<String> claims) {
}
