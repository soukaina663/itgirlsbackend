package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.GirlActionsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/girl")
@RequiredArgsConstructor
public class GirlActionsController {

    private final GirlActionsService service;

    @PostMapping("/formations/{formationId}/enroll")
    public void enroll(@AuthenticationPrincipal AuthUser me, @PathVariable Long formationId) {
        service.enrollFormation(me.id(), formationId);
    }

    @PostMapping("/events/{eventId}/register")
    public void registerEvent(@AuthenticationPrincipal AuthUser me, @PathVariable Long eventId) {
        service.registerEvent(me.id(), eventId);
    }
}
