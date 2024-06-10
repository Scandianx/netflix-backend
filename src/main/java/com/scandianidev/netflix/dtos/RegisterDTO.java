package com.scandianidev.netflix.dtos;

import com.scandianidev.netflix.model.UsuarioRole;

public record RegisterDTO(String nome, String login, String password, UsuarioRole role) {
    
}
