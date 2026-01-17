package ma.uir.itgirlsbackend.controller;

import jakarta.validation.Valid;
import ma.uir.itgirlsbackend.dto.CreateMentoringReservationRequest;
import ma.uir.itgirlsbackend.dto.MentoringReservationDto;
import ma.uir.itgirlsbackend.service.MentoringReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/mentorat")
public class MentoringReservationController {

    private final MentoringReservationService service;

    public MentoringReservationController(MentoringReservationService service) {
        this.service = service;
    }

    @PostMapping("/reservations")
    public MentoringReservationDto create(@Valid @RequestBody CreateMentoringReservationRequest req) {
        return service.create(req);
    }

    @GetMapping("/reservations")
    public List<MentoringReservationDto> list(@RequestParam Long mentorUserId) {
        return service.listAll(mentorUserId).stream().map(MentoringReservationDto::from).toList();
    }
}
