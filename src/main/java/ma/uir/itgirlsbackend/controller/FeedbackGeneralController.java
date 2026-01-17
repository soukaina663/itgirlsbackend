package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.FeedbackGeneralDto;
import ma.uir.itgirlsbackend.service.FeedbackGeneralService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/feedbackgeneral")
public class FeedbackGeneralController {

    private final FeedbackGeneralService service;

    public FeedbackGeneralController(FeedbackGeneralService service) {
        this.service = service;
    }

    @GetMapping
    public List<FeedbackGeneralDto> list() {
        return service.list();
    }
}
