package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Formation;

public record FormationDto(
        Long id,
        String themeKey,
        String themeLabel,
        String title,
        String level,
        Double rating,
        Integer reviewsCount,
        Integer enrolledCount,
        String prerequisites,
        Integer durationWeeks,
        Integer hoursPerWeek,
        Boolean selfPaced,
        Integer lessonsCount,
        String badge,
        String thumbnail,
        Boolean isPopular
) {
    public static FormationDto from(Formation f) {
        return new FormationDto(
                f.getId(),
                f.getThemeKey(),
                f.getThemeLabel(),
                f.getTitle(),
                f.getLevel(),
                f.getRating(),
                f.getReviewsCount(),
                f.getEnrolledCount(),
                f.getPrerequisites(),
                f.getDurationWeeks(),
                f.getHoursPerWeek(),
                f.getSelfPaced(),
                f.getLessonsCount(),
                f.getBadge(),
                f.getThumbnail(),
                f.getIsPopular()
        );
    }
}
