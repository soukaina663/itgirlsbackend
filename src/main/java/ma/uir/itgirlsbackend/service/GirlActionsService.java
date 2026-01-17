package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.*;
import ma.uir.itgirlsbackend.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GirlActionsService {

    private final FormationRepository formationRepository;
    private final FormationEnrollmentRepository formationEnrollmentRepository;

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;

    @Transactional
    public void enrollFormation(Long girlUserId, Long formationId) {
        Formation f = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation introuvable: " + formationId));

        if (formationEnrollmentRepository.findByFormationIdAndGirlUserId(formationId, girlUserId).isPresent()) return;

        formationEnrollmentRepository.save(FormationEnrollment.builder()
                .formationId(formationId)
                .girlUserId(girlUserId)
                .progressPercent(0)
                .build());

        Integer c = f.getEnrolledCount();
        f.setEnrolledCount(c == null ? 1 : c + 1);
        formationRepository.save(f);
    }

    @Transactional
    public void registerEvent(Long girlUserId, Long eventId) {
        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event introuvable: " + eventId));

        if (eventRegistrationRepository.findByEventIdAndGirlUserId(eventId, girlUserId).isPresent()) return;

        eventRegistrationRepository.save(EventRegistration.builder()
                .eventId(eventId)
                .girlUserId(girlUserId)
                .build());

        Integer c = e.getParticipantsCount();
        e.setParticipantsCount(c == null ? 1 : c + 1);
        eventRepository.save(e);
    }
}
