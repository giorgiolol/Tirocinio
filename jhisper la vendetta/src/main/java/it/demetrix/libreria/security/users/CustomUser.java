package it.demetrix.libreria.security.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collection;


public class CustomUser extends User implements CustomUserDetalis {
    private long id ;
    private String email;

    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities,
                      Long id){
        super(username,password,authorities);
        this.id=id;
    }
    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities,
                      Long id,
                      String email){
        super(username,password,authorities);
        this.id=id;
        this.email=email;
    }



    public CustomUser(String username,
                      String password,
                      boolean enabled,
                      boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities,
                      Long id) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
        this.email="";
    }

    public CustomUser(String username,
                      String password,
                      boolean enabled,
                      boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities,
                      Long id,
                      String email) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
        this.email=email;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
