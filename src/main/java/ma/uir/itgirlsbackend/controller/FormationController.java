package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.Formation;
import ma.uir.itgirlsbackend.dto.FormationDto;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.FormationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FormationController {

    private final FormationService formationService;

    // ---- PUBLIC ----
    @GetMapping("/public/formations")
    public List<FormationDto> list(
            @RequestParam(required = false) String themeKey,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String q
    ) {
        return formationService.list(themeKey, level, q);
    }

    @GetMapping("/public/formations/{id}")
    public FormationDto get(@PathVariable Long id) {
        return formationService.get(id);
    }

    // ---- EXPERT ----
    @PostMapping("/expert/formations")
    public FormationDto create(@AuthenticationPrincipal AuthUser me, @RequestBody Formation f) {
        f.setExpertId(me.id());
        return formationService.create(f);
    }

    @PutMapping("/expert/formations/{id}")
    public FormationDto update(@AuthenticationPrincipal AuthUser me, @PathVariable Long id, @RequestBody Formation f) {
        // (simple) on force owner
        f.setExpertId(me.id());
        return formationService.update(id, f);
    }

    @DeleteMapping("/expert/formations/{id}")
    public void delete(@PathVariable Long id) {
        formationService.delete(id);
    }
}
