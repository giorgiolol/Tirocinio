package it.demetrix.libreria.security.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetalisService {
        CustomUserDetalis loadUserByUsername(String var1) throws UsernameNotFoundException;

}
