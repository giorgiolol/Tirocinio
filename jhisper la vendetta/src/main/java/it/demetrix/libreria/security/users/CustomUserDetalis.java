package it.demetrix.libreria.security.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public interface CustomUserDetalis extends UserDetails {
    Long getId();
    String getEmail();
}
