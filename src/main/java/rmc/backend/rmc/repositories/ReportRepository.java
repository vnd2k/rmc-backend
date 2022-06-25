package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report,String> {
}
