package ma.uir.itgirlsbackend.dto;

import java.util.List;

public record GirlDashboardResponse(
        Profile profile,
        Stats stats,
        List<EnrollmentItem> enrollments,
        List<EventItem> events,
        List<ConversationItem> conversations
) {
    public record Profile(String name, String level, String badge) {}
    public record Stats(int formations, int events, int messages) {}

    public record EnrollmentItem(
            Long id, Long formationId, String formationTitle,
            String level, String themeKey, Integer progressPercent
    ) {}

    public record EventItem(Long id, String title, String startsAt, String location) {}

    public record ConversationItem(Long id, String title, String type) {}
}
