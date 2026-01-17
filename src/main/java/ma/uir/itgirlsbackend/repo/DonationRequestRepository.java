package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRequestRepository extends JpaRepository<DonationRequest, Long> {
}
