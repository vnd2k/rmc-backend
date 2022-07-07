package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.Cv;
import rmc.backend.rmc.entities.Job;
import rmc.backend.rmc.entities.RMember;

import java.util.List;

@Repository
public interface CvRepository extends JpaRepository<Cv, String> {
    boolean existsByJobAndMember(Job job, RMember member);

    List<Cv> findByJob(Job job);
}
