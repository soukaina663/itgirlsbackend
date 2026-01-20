package ma.uir.itgirlsbackend.security;

import org.springframework.security.core.GrantedAuthority;

public record AuthUser(
        Long id,
        String role,
        String name,
        String email
) {

}
