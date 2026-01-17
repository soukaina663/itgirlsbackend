package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.dto.FeedbackGeneralDto;
import ma.uir.itgirlsbackend.repo.FeedbackGeneralRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackGeneralService {

    private final FeedbackGeneralRepository repo;

    public FeedbackGeneralService(FeedbackGeneralRepository repo) {
        this.repo = repo;
    }

    public List<FeedbackGeneralDto> list() {
        return repo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(FeedbackGeneralDto::from)
                .toList();
    }
}
