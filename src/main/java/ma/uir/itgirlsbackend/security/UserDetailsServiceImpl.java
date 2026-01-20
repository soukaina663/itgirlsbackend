package ma.uir.itgirlsbackend.security;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.User;
import ma.uir.itgirlsbackend.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Email vide");
        }

        User u = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + email));

        String role = normalizeRole(String.valueOf(u.getRole()));


        // Spring Security attend ROLE_XXX pour hasRole("XXX")
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash()) // hash bcrypt en DB
                .authorities("ROLE_" + role)
                .accountLocked(false)
                .disabled(false)
                .build();
    }

    private static String normalizeRole(String role) {
        if (role == null) return "GIRL";
        String r = role.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        return r;
    }
}
