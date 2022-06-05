package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmc.backend.rmc.entities.RUser;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RUserRepository
        extends JpaRepository<RUser, String> {
    Optional<RUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE RUser a " + "SET a.enabled = TRUE WHERE a.email = ?1")
    void enableRUser(String email);
}
