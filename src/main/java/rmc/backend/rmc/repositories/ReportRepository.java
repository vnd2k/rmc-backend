package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.Rating;
import rmc.backend.rmc.entities.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,String> {
    List<Report> findAllByMember(RMember member);

    boolean existsByMember(RMember member);

    List<Report> findAllByRating(Rating rating);
}
