package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.RUser;
import rmc.backend.rmc.entities.VerifyToken;
import rmc.backend.rmc.repositories.CompanyRepository;
import rmc.backend.rmc.repositories.MemberRepository;
import rmc.backend.rmc.repositories.RUserRepository;
import rmc.backend.rmc.security.UserRole;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RUserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final RUserRepository rUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final VerifyTokenService verifyTokenService;

    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return rUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String registerUser(RUser rUser) {

//        if (request.getRole().equals(UserRole.MEMBER.toString())) {
//            memberRepository.save(new RMember(newUser));
//        } else if (request.getRole().equals(UserRole.COMPANY.toString())) {
//            companyRepository.save(new RCompany(newUser));
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role is invalid");
//        }


        boolean existUser = rUserRepository.findByEmail(rUser.getEmail()).isPresent();

        if (existUser) {
            throw new IllegalStateException("email already taken");
        }

        if (rUser.getUserRole().equals(UserRole.MEMBER)) {
            memberRepository.save(new RMember(rUser));
        } else if (rUser.getUserRole().equals(UserRole.COMPANY)) {
            companyRepository.save(new RCompany(rUser));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role is invalid");
        }

        String encoderPassword = bCryptPasswordEncoder.encode(rUser.getPassword());
        rUser.setPassword(encoderPassword);
        rUserRepository.save(rUser);

        String token = NanoIdUtils.randomNanoId();

        VerifyToken verifyToken = new VerifyToken(NanoIdUtils.randomNanoId(), token, LocalDateTime.now(), LocalDateTime.now().plusDays(365), rUser);
        verifyTokenService.saveConfirmationToken(verifyToken);

        return token;
    }

    public void enableRUser(String email) {
        rUserRepository.enableRUser(email);
    }


}
