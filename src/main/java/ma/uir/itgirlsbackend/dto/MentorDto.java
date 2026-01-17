package ma.uir.itgirlsbackend.dto;

import ma.uir.itgirlsbackend.domain.Mentor;

import java.util.Arrays;
import java.util.List;

public record MentorDto(
        Long id,
        Boolean verified,
        String name,
        String title,
        List<String> tags,
        String bio
) {
    public static MentorDto from(Mentor m) {
        List<String> tags = (m.getTagsCsv() == null || m.getTagsCsv().isBlank())
                ? List.of()
                : Arrays.stream(m.getTagsCsv().split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        Long outId = m.getUserId() != null ? m.getUserId() : m.getId();
        return new MentorDto(outId, m.getVerified(), m.getName(), m.getTitle(), tags, m.getBio());
    }
}
