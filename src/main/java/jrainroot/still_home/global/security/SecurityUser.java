package jrainroot.still_home.global.security;

import java.util.Collection;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class SecurityUser extends User {
    private final long id;

    public SecurityUser(long id,
                        String username,
                        String password,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public SecurityUser(long id,
                        String username,
                        String password,
                        boolean enabled,
                        boolean accountNonExpired,
                        boolean credentialsNonExpired,
                        boolean accountNonLocked,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Authentication genAuthentication() {
      Authentication auth = new UsernamePasswordAuthenticationToken(
              this,
              this.getPassword(),
              this.getAuthorities());
      return auth;
    }

}
