package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.User;

public record AdminUserDto(
        Long id,
        String name,
        String email,
        String role,
        Boolean isMentor,
        String level,
        String profession
) {
    public static AdminUserDto from(User u) {
        return new AdminUserDto(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole() == null ? null : u.getRole().name(),
                u.getIsMentor(),
                u.getLevel(),
                u.getProfession()
        );
    }
}
