package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.LikeRating;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.Rating;

import java.util.List;

@Repository
public interface LikeRatingRepository extends JpaRepository<LikeRating, String> {
    List<LikeRating> findAllByRating(Rating rating);

    LikeRating findByRatingAndMemberId(Rating rating, String memberId);

    boolean existsByRatingAndMemberId(Rating rating, String memberId);
}
