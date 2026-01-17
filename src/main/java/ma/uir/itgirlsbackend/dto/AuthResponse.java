package ma.uir.itgirlsbackend.dto;

public record AuthResponse(
        String token,
        Long id,
        String name,
        String email,
        String role,      // "EXPERT" | "GIRL" | "ADMIN"
        String level,     // GIRL
        String profession,// EXPERT
        Boolean isMentor, // EXPERT
        String cvFile     // EXPERT
) {}
