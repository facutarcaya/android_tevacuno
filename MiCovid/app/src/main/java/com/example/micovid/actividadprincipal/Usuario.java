package com.example.micovid.actividadprincipal;

public class Usuario {

    private String email;
    private String token;
    private String tokenRefresh;

    public Usuario(String email, String token, String tokenRefresh) {
        this.email = email;
        this.token = token;
        this.tokenRefresh = tokenRefresh;
    }

    public void refrescarToken() {
        String nuevoTokenRefesh = "NADA";
        //Llamar API REST

        this.tokenRefresh = nuevoTokenRefesh;
    }
}
