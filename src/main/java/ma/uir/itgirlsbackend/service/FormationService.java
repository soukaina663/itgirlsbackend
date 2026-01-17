package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.Formation;
import ma.uir.itgirlsbackend.dto.FormationDto;
import ma.uir.itgirlsbackend.repo.FormationRepository;
import ma.uir.itgirlsbackend.repo.FormationSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormationService {

    private final FormationRepository formationRepository;

    public List<FormationDto> list(String themeKey, String level, String q) {
        Specification<Formation> spec = Specification
                .where(FormationSpecs.themeKeyEquals(themeKey))
                .and(FormationSpecs.levelEquals(level))
                .and(FormationSpecs.qLike(q));

        return formationRepository.findAll(spec).stream().map(FormationDto::from).toList();
    }

    public FormationDto get(Long id) {
        Formation f = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation introuvable: " + id));
        return FormationDto.from(f);
    }

    // CRUD pour expert
    public FormationDto create(Formation f) {
        return FormationDto.from(formationRepository.save(f));
    }

    public FormationDto update(Long id, Formation updated) {
        Formation f = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation introuvable: " + id));

        f.setThemeKey(updated.getThemeKey());
        f.setThemeLabel(updated.getThemeLabel());
        f.setTitle(updated.getTitle());
        f.setLevel(updated.getLevel());
        f.setRating(updated.getRating());
        f.setReviewsCount(updated.getReviewsCount());
        f.setEnrolledCount(updated.getEnrolledCount());
        f.setPrerequisites(updated.getPrerequisites());
        f.setDurationWeeks(updated.getDurationWeeks());
        f.setHoursPerWeek(updated.getHoursPerWeek());
        f.setSelfPaced(updated.getSelfPaced());
        f.setLessonsCount(updated.getLessonsCount());
        f.setBadge(updated.getBadge());
        f.setThumbnail(updated.getThumbnail());
        f.setIsPopular(updated.getIsPopular());

        return FormationDto.from(formationRepository.save(f));
    }

    public void delete(Long id) {
        formationRepository.deleteById(id);
    }
}

