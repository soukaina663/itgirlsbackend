package ma.uir.itgirlsbackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = Long.valueOf(claims.getSubject());
        String role = String.valueOf(claims.get("role"));
        String name = String.valueOf(claims.get("name"));
        String email = String.valueOf(claims.get("email"));

        return new AuthUser(id, role, name, email);
    }
}
