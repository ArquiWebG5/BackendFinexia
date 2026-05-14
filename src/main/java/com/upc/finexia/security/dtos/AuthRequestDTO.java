package com.upc.finexia.security.dtos;

// DTO de credenciales para inicio de sesion (HU 02 - Inicio de sesion).
public class AuthRequestDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
