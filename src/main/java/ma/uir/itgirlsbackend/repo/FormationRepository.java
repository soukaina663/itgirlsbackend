package ma.uir.itgirlsbackend.repo;

import ma.uir.itgirlsbackend.domain.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long>, JpaSpecificationExecutor<Formation> {

    long countByExpertId(Long expertId);

    List<Formation> findTop3ByExpertIdAndIsPopularTrueOrderByIdDesc(Long expertId);

    List<Formation> findTop3ByExpertIdOrderByIdDesc(Long expertId);
}
