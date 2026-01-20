package ma.uir.itgirlsbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final Key key;
    private final long expMillis;

    public JwtService(
            @Value("${app.jwt.secret:CHANGE_ME_TO_A_LONG_SECRET_KEY_32CHARS_MIN}") String secret,
            @Value("${app.jwt.exp-minutes:720}") long expMinutes
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("app.jwt.secret doit faire au moins 32 caractères");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMillis = expMinutes * 60L * 1000L;
    }

    public String generateToken(AuthUser user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(user.id()))
                .claim("role", user.role())
                .claim("name", user.name())
                .claim("email", user.email())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public AuthUser verifyAndGetUser(String token) {
        Claims claims = parseClaims(token);

        Long id = Long.valueOf(claims.getSubject());
        String role = String.valueOf(claims.get("role"));
        String name = String.valueOf(claims.get("name"));
        String email = String.valueOf(claims.get("email"));

        return new AuthUser(id, role, name, email);
    }

    // ------------------------------------------------------------------
    // ✅ AJOUTS pour ton JwtAuthFilter actuel (extractUsername/isTokenValid/buildAuthentication)
    // ------------------------------------------------------------------

    /**
     * Ton UserDetailsServiceImpl charge généralement par email.
     * Donc on retourne le claim "email".
     */
    public String extractUsername(String token) {
        try {
            Claims claims = parseClaims(token);
            Object email = claims.get("email");
            return email == null ? null : String.valueOf(email);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Vérifie signature + expiration + correspondance username
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);

            Date exp = claims.getExpiration();
            if (exp == null || exp.before(new Date())) return false;

            String username = extractUsername(token);
            if (username == null) return false;

            return username.equalsIgnoreCase(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Construit une Authentication compatible hasRole(...)
     * On met ROLE_<role> (ex: ROLE_GIRL / ROLE_EXPERT / ROLE_ADMIN)
     */
    public UsernamePasswordAuthenticationToken buildAuthentication(String token, UserDetails userDetails) {
        AuthUser u = verifyAndGetUser(token);

        String role = normalizeRole(u.role());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        // principal = AuthUser (comme tu utilises @AuthenticationPrincipal AuthUser)
        return new UsernamePasswordAuthenticationToken(u, null, authorities);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static String normalizeRole(String role) {
        if (role == null) return "GIRL";
        String r = role.trim().toUpperCase();
        if (r.startsWith("ROLE_")) r = r.substring(5);
        return r;
    }
}
