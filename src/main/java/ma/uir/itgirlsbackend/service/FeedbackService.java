package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.Feedback;
import ma.uir.itgirlsbackend.dto.CreateFeedbackRequest;
import ma.uir.itgirlsbackend.dto.FeedbackDto;
import ma.uir.itgirlsbackend.repo.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public List<FeedbackDto> list() {
        return repo.findAllByOrderByCreatedAtDesc()
                .stream().map(FeedbackDto::from).toList();
    }

    public FeedbackDto create(CreateFeedbackRequest req) {
        // simple validations
        if (req.name() == null || req.name().isBlank()) throw new RuntimeException("name obligatoire");
        if (req.text() == null || req.text().isBlank()) throw new RuntimeException("text obligatoire");
        if (req.stars() == null || req.stars() < 1 || req.stars() > 5) throw new RuntimeException("stars doit être entre 1 et 5");

        Feedback f = Feedback.builder()
                .name(req.name().trim())
                .text(req.text().trim())
                .stars(req.stars())
                .build();

        return FeedbackDto.from(repo.save(f));
    }

    public FeedbackDto update(Long id, CreateFeedbackRequest req) {
        Feedback f = repo.findById(id).orElseThrow(() -> new RuntimeException("Feedback introuvable: " + id));

        if (req.name() != null && !req.name().isBlank()) f.setName(req.name().trim());
        if (req.text() != null && !req.text().isBlank()) f.setText(req.text().trim());
        if (req.stars() != null) {
            if (req.stars() < 1 || req.stars() > 5) throw new RuntimeException("stars doit être entre 1 et 5");
            f.setStars(req.stars());
        }

        return FeedbackDto.from(repo.save(f));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
