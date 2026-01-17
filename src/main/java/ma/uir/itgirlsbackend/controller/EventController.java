package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.EventDto;
import ma.uir.itgirlsbackend.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDto> list(@RequestParam(required = false) String typeKey) {
        return eventService.list(typeKey).stream().map(EventDto::from).toList();
    }
}
