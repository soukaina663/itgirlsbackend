package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.User;
import ma.uir.itgirlsbackend.domain.enums.UserRole;
import ma.uir.itgirlsbackend.dto.AuthResponse;
import ma.uir.itgirlsbackend.repo.UserRepository;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ma.uir.itgirlsbackend.domain.Mentor;
import ma.uir.itgirlsbackend.repo.MentorRepository;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final MentorRepository mentorRepository;


    public AuthService(UserRepository userRepo, MentorRepository mentorRepository, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.mentorRepository = mentorRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    private static String s(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
    }

    private static boolean b(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean bo) return bo;
        String str = String.valueOf(v).trim().toLowerCase();
        return str.equals("true") || str.equals("1") || str.equals("yes") || str.equals("on");
    }

    private static UserRole parseRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) return UserRole.GIRL;
        try {
            return UserRole.valueOf(roleStr.trim().toUpperCase());
        } catch (Exception e) {
            return UserRole.GIRL;
        }
    }

    public AuthResponse register(Object nameObj,
                                 Object emailObj,
                                 Object passwordObj,
                                 Object roleObj,
                                 Object levelObj,
                                 Object professionObj,
                                 Object isMentorObj,
                                 Object cvFileObj) {

        String name = s(nameObj);
        String email = s(emailObj).toLowerCase();
        String password = s(passwordObj);
        UserRole role = parseRole(s(roleObj));

        String level = s(levelObj);
        String profession = s(professionObj);
        boolean isMentor = b(isMentorObj);
        String cvFile = s(cvFileObj);

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            throw new RuntimeException("name/email/password required");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("email already used");
        }

        User u = User.builder()
                .name(name)
                .email(email)
                .passwordHash(encoder.encode(password))
                .role(role)
                .level(role == UserRole.GIRL ? (level.isBlank() ? "lyceenne" : level) : null)
                .profession((role == UserRole.EXPERT || role == UserRole.ADMIN) ? (profession.isBlank() ? "Expert IT" : profession) : null)
                .isMentor((role == UserRole.EXPERT || role == UserRole.ADMIN) ? isMentor : false)
                .cvFile((role == UserRole.EXPERT || role == UserRole.ADMIN) ? (cvFile.isBlank() ? null : cvFile) : null)
                .build();

        u = userRepo.save(u);

        if ((u.getRole() == UserRole.EXPERT || u.getRole() == UserRole.ADMIN) && Boolean.TRUE.equals(u.getIsMentor())) {
            if (mentorRepository.findByUserId(u.getId()).isEmpty()) {
                mentorRepository.save(Mentor.builder()
                        .userId(u.getId())
                        .verified(false)
                        .name(u.getName())
                        .title(u.getProfession() == null ? "Mentor" : u.getProfession())
                        .bio("")
                        .tagsCsv("")
                        .build());
            }
        }
        String outRole = (u.getRole() == UserRole.ADMIN) ? "ADMIN" : u.getRole().name();

        AuthUser authUser = new AuthUser(u.getId(), outRole, u.getName(), u.getEmail());
        String token = jwtService.generateToken(authUser);

        return new AuthResponse(
                token,
                u.getId(),
                u.getName(),
                u.getEmail(),
                outRole,
                u.getLevel(),
                u.getProfession(),
                u.getIsMentor(),
                u.getCvFile()
        );
    }


    public AuthResponse login(Object emailObj, Object passwordObj) {
        String email = s(emailObj).toLowerCase();
        String password = s(passwordObj);

        if (email.isBlank() || password.isBlank()) {
            throw new RuntimeException("invalid credentials");
        }

        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("invalid credentials"));

        if (u.getPasswordHash() == null || !encoder.matches(password, u.getPasswordHash())) {
            throw new RuntimeException("invalid credentials");
        }

        String outRole = (u.getRole() == UserRole.ADMIN) ? "ADMIN" : u.getRole().name();

        AuthUser authUser = new AuthUser(u.getId(), outRole, u.getName(), u.getEmail());
        String token = jwtService.generateToken(authUser);

        return new AuthResponse(
                token,
                u.getId(),
                u.getName(),
                u.getEmail(),
                outRole,
                u.getLevel(),
                u.getProfession(),
                u.getIsMentor(),
                u.getCvFile()
        );
    }
}
