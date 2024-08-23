package com.sys.park.app.dtos.Auth;

public record LoginResponse(String accessToken, Long expiresIn, String username, String name, String role) {
    
}
