package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.dto.AuthResponse;
import ma.uir.itgirlsbackend.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static Object get(Map<String, Object> body, String key) {
        return body == null ? null : body.get(key);
    }

    private static Object getOrDefault(Map<String, Object> body, String key, Object def) {
        if (body == null) return def;
        Object v = body.get(key);
        return v == null ? def : v;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody Map<String, Object> body) {

        // Champs communs
        Object name = get(body, "name");
        Object email = get(body, "email");
        Object password = get(body, "password");
        Object role = getOrDefault(body, "role", "GIRL");

        // ✅ GIRL : level = niveau scolaire (lyceenne/bac2/bac3/master/autre)
        Object level = getOrDefault(body, "level", "");

        // ✅ EXPERT : profession + isMentor + cvFile
        Object profession = getOrDefault(body, "profession", "");
        Object isMentor = getOrDefault(body, "isMentor", false);

        // selon ton front : parfois "cvFileName" au lieu de "cvFile"
        Object cvFile = getOrDefault(body, "cvFile", getOrDefault(body, "cvFileName", ""));

        return authService.register(
                name,
                email,
                password,
                role,
                level,
                profession,
                isMentor,
                cvFile
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody Map<String, Object> body) {
        return authService.login(
                get(body, "email"),
                get(body, "password")
        );
    }

    /**
     * OAuth optionnel
     * Tu peux créer une GIRL par défaut, en stockant level (niveau scolaire).
     * Front enverra: { email, name, level? }
     */
    @PostMapping("/oauth")
    public AuthResponse oauth(@RequestBody Map<String, Object> body) {

        // pour OAuth on crée une GIRL
        Object name = getOrDefault(body, "name", "User");
        Object email = get(body, "email");

        // si absent -> lyceenne
        Object level = getOrDefault(body, "level", "lyceenne");

        // password fictif (pas utilisé)
        Object password = "OAUTH_" + System.currentTimeMillis();

        return authService.register(
                name,
                email,
                password,
                "GIRL",
                level,
                "",     // profession
                false,  // isMentor
                ""      // cvFile
        );
    }
}
