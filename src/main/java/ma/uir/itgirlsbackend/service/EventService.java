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

    /**
     * Récupère la liste des events.
     * - si typeKey vide/null => retourne tout trié par date
     * - sinon => filtre par typeKey trié par date
     */
    public List<Event> list(String typeKey) {
        if (typeKey == null || typeKey.isBlank() || "all".equalsIgnoreCase(typeKey.trim())) {
            return eventRepository.findAllByOrderByStartsAtAsc();
        }
        return eventRepository.findByTypeKeyIgnoreCaseOrderByStartsAtAsc(typeKey.trim());
    }
}
