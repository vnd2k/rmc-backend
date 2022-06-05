package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmc.backend.rmc.entities.Company;

@Repository
@Transactional(readOnly = true)
public interface CompanyRepository extends JpaRepository<Company, String> {
}
