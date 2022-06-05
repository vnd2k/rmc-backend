package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmc.backend.rmc.entities.VerifyToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, String> {
    Optional<VerifyToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE VerifyToken v " +
            "SET v.confirmedAt = ?2 " +
            "WHERE v.token = ?1")
    void updateConfirmedAt(String token,
                           LocalDateTime confirmedAt);
}
