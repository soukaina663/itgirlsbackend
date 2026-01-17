package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.CreateFeedbackRequest;
import ma.uir.itgirlsbackend.dto.FeedbackDto;
import ma.uir.itgirlsbackend.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    // ✅ Public (Front Mentorat)
    @GetMapping("/api/public/feedbacks")
    public List<FeedbackDto> list() {
        return service.list();
    }

    // ✅ CRUD (si tu veux laisser expert/admin gérer)
    @PostMapping("/api/expert/feedbacks")
    public FeedbackDto create(@RequestBody CreateFeedbackRequest req) {
        return service.create(req);
    }

    @PutMapping("/api/expert/feedbacks/{id}")
    public FeedbackDto update(@PathVariable Long id, @RequestBody CreateFeedbackRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/api/expert/feedbacks/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
