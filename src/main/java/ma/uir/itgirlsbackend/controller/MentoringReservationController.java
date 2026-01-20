package ma.uir.itgirlsbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.enums.MentoringStatus;
import ma.uir.itgirlsbackend.dto.CreateMentoringReservationRequest;
import ma.uir.itgirlsbackend.dto.MentoringReservationDto;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.MentoringReservationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MentoringReservationController {

    private final MentoringReservationService service;

    // ✅ GIRL connectée : créer réservation (DTO unique)
    @PostMapping("/girl/mentorat/reservations")
    public MentoringReservationDto create(@AuthenticationPrincipal AuthUser me,
                                          @Valid @RequestBody CreateMentoringReservationRequest req) {
        if (me == null || me.role() == null || !me.role().equalsIgnoreCase("GIRL")) {
            throw new RuntimeException("Accès refusé (GIRL uniquement)");
        }
        return service.createForGirl(me.id(), me.name(), req);
    }

    // ✅ GIRL : mes réservations
    @GetMapping("/girl/mentorat/reservations")
    public List<MentoringReservationDto> myReservations(@AuthenticationPrincipal AuthUser me) {
        if (me == null || me.role() == null || !me.role().equalsIgnoreCase("GIRL")) {
            throw new RuntimeException("Accès refusé (GIRL uniquement)");
        }
        return service.listForGirl(me.id()).stream().map(MentoringReservationDto::from).toList();
    }

    // ✅ EXPERT mentor : ses réservations
    @GetMapping("/mentor/mentorat/reservations")
    public List<MentoringReservationDto> mentorReservations(@AuthenticationPrincipal AuthUser me) {
        if (me == null || me.role() == null || !me.role().equalsIgnoreCase("EXPERT")) {
            throw new RuntimeException("Accès refusé (EXPERT uniquement)");
        }
        return service.listForMentor(me.id()).stream().map(MentoringReservationDto::from).toList();
    }

    // ✅ EXPERT mentor : update status
    @PatchMapping("/mentor/mentorat/reservations/{id}/status")
    public MentoringReservationDto updateStatus(@AuthenticationPrincipal AuthUser me,
                                                @PathVariable Long id,
                                                @RequestBody Map<String, Object> body) {
        if (me == null || me.role() == null || !me.role().equalsIgnoreCase("EXPERT")) {
            throw new RuntimeException("Accès refusé (EXPERT uniquement)");
        }

        String s = body == null ? "" : String.valueOf(body.get("status")).trim().toUpperCase();
        MentoringStatus next;
        try {
            next = MentoringStatus.valueOf(s);
        } catch (Exception e) {
            throw new RuntimeException("invalid status (EN_ATTENTE/CONFIRME/REFUSE)");
        }
        return service.updateStatus(id, me.id(), next);
    }
}
