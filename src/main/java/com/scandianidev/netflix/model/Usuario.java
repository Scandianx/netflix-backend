package com.scandianidev.netflix.model;

import java.util.Collection;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
@Entity
@SequenceGenerator(name= "generator_usuario", sequenceName="sequence_usuario", initialValue= 1, allocationSize= 1)
public class Usuario implements UserDetails{

@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_usuario")
private Integer id;
@Column(nullable=false)
private String nome;
@Column(nullable=false, unique = true)
private String login;
@Column(nullable=true)
private String password;
@Column
private UsuarioRole role;
public Usuario() {
}

public Usuario(String nome, String login, String password, UsuarioRole role) {
    this.nome = nome;
    this.login = login;
    this.password = password;
    this.role= role;
}

public int getId() {
    return id;
}

public String getEmail() {
    return login;
}

public void setId(int id) {
    this.id = id;
}
public void setNome(String nome) {
    this.nome = nome;
}
public void setEmail(String email) {
    this.login = email;
}
public void setSenha(String password) {
    this.password = password;
}
//IMPLEMENTACAO USERDATAILS
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    if(this.role == UsuarioRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
}

@Override
public String getPassword() {
 
    return password;
}
@Override
public String getUsername() {
    
    return nome;
}
@Override
public boolean isAccountNonExpired() {
    
    return true;
}
@Override
public boolean isAccountNonLocked() {
   
    return true;
}
@Override
public boolean isCredentialsNonExpired() {
   
    return true;
}
@Override
public boolean isEnabled() {
   
    return true;
}



}
