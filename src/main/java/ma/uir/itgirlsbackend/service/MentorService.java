package ma.uir.itgirlsbackend.service;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.dto.MentorDto;
import ma.uir.itgirlsbackend.repo.MentorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    public List<MentorDto> list(Boolean verified) {
        if (verified == null) {
            return mentorRepository.findAllByOrderByVerifiedDescNameAsc()
                    .stream().map(MentorDto::from).toList();
        }
        return mentorRepository.findByVerified(verified)
                .stream().map(MentorDto::from).toList();
    }
}
