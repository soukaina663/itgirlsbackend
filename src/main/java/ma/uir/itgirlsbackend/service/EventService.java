package ma.uir.itgirlsbackend.service;

import ma.uir.itgirlsbackend.domain.Event;
import ma.uir.itgirlsbackend.repo.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> list(String typeKey) {
        if (typeKey == null || typeKey.isBlank() || "all".equalsIgnoreCase(typeKey.trim())) {
            return eventRepository.findAllByOrderByStartsAtAsc();
        }
        return eventRepository.findByTypeKeyIgnoreCaseOrderByStartsAtAsc(typeKey.trim());
    }

    public List<Event> listByExpert(Long expertId) {
        return eventRepository.findByExpertIdOrderByStartsAtAsc(expertId);
    }

    public Event create(Event e) {
        if (e.getExpertId() == null) throw new RuntimeException("expertId requis");
        if (e.getTitle() == null || e.getTitle().isBlank()) throw new RuntimeException("title requis");
        if (e.getTypeKey() == null || e.getTypeKey().isBlank()) throw new RuntimeException("typeKey requis");
        if (e.getTypeLabel() == null || e.getTypeLabel().isBlank()) e.setTypeLabel(e.getTypeKey());
        if (e.getStartsAt() == null) throw new RuntimeException("startsAt requis");
        return eventRepository.save(e);
    }

    public Event update(Long id, Event patch, Long expertId) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("event introuvable: " + id));

        if (expertId != null && !expertId.equals(existing.getExpertId())) {
            throw new RuntimeException("Accès refusé (pas propriétaire)");
        }

        if (patch.getTypeKey() != null) existing.setTypeKey(patch.getTypeKey());
        if (patch.getTypeLabel() != null) existing.setTypeLabel(patch.getTypeLabel());
        if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
        if (patch.getStartsAt() != null) existing.setStartsAt(patch.getStartsAt());
        if (patch.getDurationMins() != null) existing.setDurationMins(patch.getDurationMins());
        if (patch.getParticipantsCount() != null) existing.setParticipantsCount(patch.getParticipantsCount());
        if (patch.getBadge() != null) existing.setBadge(patch.getBadge());
        if (patch.getCover() != null) existing.setCover(patch.getCover());

        return eventRepository.save(existing);
    }

    public void delete(Long id, Long expertId) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("event introuvable: " + id));

        if (expertId != null && !expertId.equals(existing.getExpertId())) {
            throw new RuntimeException("Accès refusé (pas propriétaire)");
        }
        eventRepository.deleteById(id);
    }
}
