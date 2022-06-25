package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findAllByCompanyOrderByCreatedAtDesc(RCompany company);

    List<Rating> findAllByMemberOrderByCreatedAtDesc(RMember member);

    Rating findByIdAndMember(String id, RMember member);
}
