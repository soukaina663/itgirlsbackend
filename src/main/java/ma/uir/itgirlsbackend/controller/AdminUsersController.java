package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.User;
import ma.uir.itgirlsbackend.domain.enums.UserRole;
import ma.uir.itgirlsbackend.dto.AdminUserDto;
import ma.uir.itgirlsbackend.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUsersController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PatchMapping("/users/{id}/role")
    public AdminUserDto updateRole(@PathVariable Long id, @RequestBody Map<String,Object> body) {
        String roleStr = body == null ? "" : String.valueOf(body.get("role"));
        UserRole role;
        try {
            role = UserRole.valueOf(roleStr.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("role invalide (GIRL/EXPERT/ADMIN)");
        }

        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User introuvable: " + id));
        u.setRole(role);

        if (role == UserRole.GIRL) {
            u.setProfession(null);
            u.setIsMentor(false);
            u.setCvFile(null);
            if (u.getLevel() == null) u.setLevel("lyceenne");
        }
        if (role == UserRole.EXPERT || role == UserRole.ADMIN) {
            if (u.getProfession() == null) u.setProfession("Expert IT");
        }
        return AdminUserDto.from(userRepository.save(u));
    }

    @PostMapping("/admins")
    public AdminUserDto createAdmin(@RequestBody Map<String,Object> body) {
        String name = body == null ? "" : String.valueOf(body.get("name")).trim();
        String email = body == null ? "" : String.valueOf(body.get("email")).trim().toLowerCase();
        String password = body == null ? "" : String.valueOf(body.get("password")).trim();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            throw new RuntimeException("name/email/password requis");
        }
        if (userRepository.existsByEmail(email)) throw new RuntimeException("email déjà utilisé");

        User u = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(UserRole.ADMIN)
                .profession("Admin")
                .isMentor(false)
                .build();

        return AdminUserDto.from(userRepository.save(u));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
