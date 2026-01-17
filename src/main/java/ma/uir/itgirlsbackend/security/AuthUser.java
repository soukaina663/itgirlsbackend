package ma.uir.itgirlsbackend.security;

public record AuthUser(
        Long id,
        String role,
        String name,
        String email
) {}
