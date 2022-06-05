package rmc.backend.rmc.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rmc.backend.rmc.entities.VerifyToken;
import rmc.backend.rmc.repositories.VerifyTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;

    public void saveConfirmationToken(VerifyToken token) {
        verifyTokenRepository.save(token);
    }

    public Optional<VerifyToken> getToken(String token) {
        return verifyTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        verifyTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
