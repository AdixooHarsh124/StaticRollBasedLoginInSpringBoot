package com.registration.model;

public class JwtResponse {
    String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public JwtResponse(String token) {
        this.token = token;
    }
}
