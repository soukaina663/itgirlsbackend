package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Formation;
import org.springframework.data.jpa.domain.Specification;

public final class FormationSpecs {
    private FormationSpecs() {}

    public static Specification<Formation> themeKeyEquals(String themeKey) {
        return (root, query, cb) ->
                (themeKey == null || themeKey.isBlank() || "all".equalsIgnoreCase(themeKey))
                        ? cb.conjunction()
                        : cb.equal(root.get("themeKey"), themeKey);
    }

    public static Specification<Formation> levelEquals(String level) {
        return (root, query, cb) ->
                (level == null || level.isBlank() || "all".equalsIgnoreCase(level))
                        ? cb.conjunction()
                        : cb.equal(root.get("level"), level);
    }

    public static Specification<Formation> qLike(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return cb.conjunction();
            String like = "%" + q.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("themeLabel")), like)
            );
        };
    }
}

