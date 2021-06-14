package com.example.micovid.actividadprincipal;

import com.example.micovid.comm.Communication;

public class Usuario {

    private String email;
    private String token;
    private String tokenRefresh;

    public Usuario(String email, String token, String tokenRefresh) {
        this.email = email;
        this.token = token;
        this.tokenRefresh = tokenRefresh;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getTokenRefresh() {
        return tokenRefresh;
    }
}
