package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.dto.MentorDto;
import ma.uir.itgirlsbackend.service.MentorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/mentors")
public class MentorController {

    private final MentorService mentorService;

    // ex: /api/public/mentors?verified=true
    @GetMapping
    public List<MentorDto> list(@RequestParam(required = false) Boolean verified) {
        return mentorService.list(verified);
    }
}
