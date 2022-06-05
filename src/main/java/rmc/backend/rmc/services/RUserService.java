package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rmc.backend.rmc.entities.RUser;
import rmc.backend.rmc.entities.VerifyToken;
import rmc.backend.rmc.repositories.RUserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RUserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final RUserRepository rUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final VerifyTokenService verifyTokenService;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return rUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String registerUser(RUser rUser) {
        boolean existUser = rUserRepository
                .findByEmail(rUser.getEmail())
                .isPresent();

        if (existUser) {
            throw new IllegalStateException("email already taken");
        }

        String encoderPassword =
                bCryptPasswordEncoder.encode(rUser.getPassword());
        rUser.setPassword(encoderPassword);
        rUserRepository.save(rUser);

        String token = NanoIdUtils.randomNanoId();

        VerifyToken verifyToken = new VerifyToken(
                NanoIdUtils.randomNanoId(),
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(365),
                rUser
        );
        verifyTokenService.saveConfirmationToken(verifyToken);

        return token;
    }

    public void enableRUser(String email) {
        rUserRepository.enableRUser(email);
    }


}
