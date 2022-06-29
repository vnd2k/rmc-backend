package rmc.backend.rmc.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findAllByCompanyOrderByCreatedAtDesc(RCompany company);

    List<Rating> findAllByMemberOrderByCreatedAtDesc(RMember member);

    Rating findByIdAndMember(String id, RMember member);

    Optional<Rating> findByCompanyAndMember(RCompany company, RMember member);

    List<Rating> findAllByCompany(RCompany company, Pageable pageable);
}
