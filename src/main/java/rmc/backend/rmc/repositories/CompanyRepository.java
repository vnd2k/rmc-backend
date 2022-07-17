package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmc.backend.rmc.entities.RCompany;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<RCompany, String> {
    List<RCompany> findByNameContainingIgnoreCase(String character);
}
