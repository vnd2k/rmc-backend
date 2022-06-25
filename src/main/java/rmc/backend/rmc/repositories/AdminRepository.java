package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.RAdmin;

@Repository
public interface AdminRepository extends JpaRepository<RAdmin, String> {
}
