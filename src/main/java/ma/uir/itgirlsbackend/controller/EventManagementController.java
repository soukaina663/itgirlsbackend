package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.Event;
import ma.uir.itgirlsbackend.dto.EventDto;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.EventService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventManagementController {

    private final EventService eventService;

    @GetMapping("/expert/events")
    public List<EventDto> myEvents(@AuthenticationPrincipal AuthUser me) {
        return eventService.listByExpert(me.id()).stream().map(EventDto::from).toList();
    }

    @PostMapping("/expert/events")
    public EventDto create(@AuthenticationPrincipal AuthUser me, @RequestBody Event e) {
        e.setExpertId(me.id());
        return EventDto.from(eventService.create(e));
    }

    @PutMapping("/expert/events/{id}")
    public EventDto update(@AuthenticationPrincipal AuthUser me, @PathVariable Long id, @RequestBody Event e) {
        return EventDto.from(eventService.update(id, e, me.id()));
    }

    @DeleteMapping("/expert/events/{id}")
    public void delete(@AuthenticationPrincipal AuthUser me, @PathVariable Long id) {
        eventService.delete(id, me.id());
    }

    @DeleteMapping("/admin/events/{id}")
    public void adminDelete(@PathVariable Long id) {
        eventService.delete(id, null);
    }
}
