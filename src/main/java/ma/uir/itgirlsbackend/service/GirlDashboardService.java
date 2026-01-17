package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.Event;
import ma.uir.itgirlsbackend.domain.Formation;
import ma.uir.itgirlsbackend.domain.User;
import ma.uir.itgirlsbackend.dto.ConversationSummaryDto;
import ma.uir.itgirlsbackend.dto.GirlDashboardResponse;
import ma.uir.itgirlsbackend.repo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GirlDashboardService {

    private final UserRepository userRepository;
    private final FormationRepository formationRepository;
    private final FormationEnrollmentRepository formationEnrollmentRepository;
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final MessageService messageService;

    public GirlDashboardResponse getDashboard(Long girlUserId) {
        User u = userRepository.findById(girlUserId)
                .orElseThrow(() -> new RuntimeException("User introuvable: " + girlUserId));

        var enrollments = formationEnrollmentRepository.findByGirlUserIdOrderByEnrolledAtDesc(girlUserId)
                .stream()
                .map(en -> {
                    Formation f = formationRepository.findById(en.getFormationId()).orElse(null);
                    String title = f != null ? f.getTitle() : "Formation";
                    String level = f != null ? f.getLevel() : "";
                    String themeKey = f != null ? f.getThemeKey() : "";
                    return new GirlDashboardResponse.EnrollmentItem(
                            en.getId(), en.getFormationId(), title, level, themeKey, en.getProgressPercent()
                    );
                })
                .toList();

        var events = eventRegistrationRepository.findByGirlUserIdOrderByRegisteredAtDesc(girlUserId)
                .stream()
                .map(reg -> eventRepository.findById(reg.getEventId()).orElse(null))
                .filter(e -> e != null)
                .map((Event e) -> new GirlDashboardResponse.EventItem(
                        e.getId(), e.getTitle(),
                        e.getStartsAt() == null ? null : e.getStartsAt().toString(),
                        "En ligne"
                ))
                .toList();

        List<ConversationSummaryDto> convs = messageService.listInbox(girlUserId);
        var convItems = convs.stream()
                .map(c -> new GirlDashboardResponse.ConversationItem(c.id(), c.title(), "mentor"))
                .toList();

        return new GirlDashboardResponse(
                new GirlDashboardResponse.Profile(u.getName(), u.getLevel(), "Girl Mode"),
                new GirlDashboardResponse.Stats(
                        (int) formationEnrollmentRepository.countByGirlUserId(girlUserId),
                        (int) eventRegistrationRepository.countByGirlUserId(girlUserId),
                        convItems.size()
                ),
                enrollments,
                events,
                convItems
        );
    }
}
