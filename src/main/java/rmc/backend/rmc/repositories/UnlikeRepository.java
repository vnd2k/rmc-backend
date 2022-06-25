package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.Rating;
import rmc.backend.rmc.entities.UnlikeRating;

import java.util.List;

@Repository
public interface UnlikeRepository extends JpaRepository<UnlikeRating, String> {
    List<UnlikeRating> findAllByRating(Rating rating);

    UnlikeRating findByRatingAndMemberId(Rating rating, String memberId);

    boolean existsByRatingAndMemberId(Rating rating, String memberId);
}
