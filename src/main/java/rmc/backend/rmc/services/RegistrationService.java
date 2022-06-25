package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RUser;
import rmc.backend.rmc.entities.VerifyToken;
import rmc.backend.rmc.entities.dto.LoginRequest;
import rmc.backend.rmc.entities.dto.LoginResponse;
import rmc.backend.rmc.entities.dto.RegisterRequest;
import rmc.backend.rmc.entities.dto.UserDetailsResponse;
import rmc.backend.rmc.repositories.CompanyRepository;
import rmc.backend.rmc.repositories.MemberRepository;
import rmc.backend.rmc.repositories.RUserRepository;
import rmc.backend.rmc.security.JwtProvider;
import rmc.backend.rmc.security.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RUserService rUserService;

    private final VerifyTokenService verifyTokenService;

    private final RUserRepository userRepository;


    private final AuthenticationManager authenticationManager;


    private final JwtProvider jwtProvider = new JwtProvider();

    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;

    @Value("${email.baseUrl}")
    private String baseUrl;

    private final EmailService emailService;

    public RegistrationService(RUserService rUserService, VerifyTokenService verifyTokenService, RUserRepository userRepository, AuthenticationManager authenticationManager, MemberRepository memberRepository, CompanyRepository companyRepository, EmailService emailService) {
        this.rUserService = rUserService;
        this.verifyTokenService = verifyTokenService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.emailService = emailService;
    }

    public String register(RegisterRequest request) {
        RUser newUser = new RUser(NanoIdUtils.randomNanoId(), request.getEmail(), request.getPassword(), request.getRole(), LocalDateTime.now());

        String token = rUserService.registerUser(newUser);

        VerifyToken verifyToken = verifyTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (verifyToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = verifyToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        verifyTokenService.setConfirmedAt(token);

        rUserService.enableRUser(verifyToken.getRUser().getEmail());

        return "confirmed";
//        String link = baseUrl + "/auth/register/confirm?token=" + token;
//        emailService.send(request.getEmail(), buildEmail(link));
//        return token;
    }

    @Transactional
    public String verifyToken(String token) {
        VerifyToken verifyToken = verifyTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (verifyToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = verifyToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        verifyTokenService.setConfirmedAt(token);

        rUserService.enableRUser(verifyToken.getRUser().getEmail());

        return "confirmed";
    }

    private String buildEmail(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n" + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n" + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" + "    <tbody><tr>\n" + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n" + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" + "          <tbody><tr>\n" + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n" + "                    <td style=\"padding-left:10px\">\n" + "                  \n" + "                    </td>\n" + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">RMC email verification</span>\n" + "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n" + "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n" + "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n" + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" + "      <td>\n" + "        \n" + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" + "                  <tbody><tr>\n" + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" + "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n" + "  </tbody></table>\n" + "\n" + "\n" + "\n" + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" + "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "    <tr>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" + "        \n" + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n<p>See you soon</p>" + "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n" + "    </tr>\n" + "    <tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>";
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) throws IllegalAccessException {
        Optional<RUser> rUser = userRepository.findByEmail(request.getEmail());
        if (rUser.isPresent()) {
            if (!rUser.get().getEnabled()) {
                throw new IllegalAccessException("Unverified email");
            }
            boolean isAdmin = rUser.get().getUserRole().equals(UserRole.ADMIN);

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String accessToken = jwtProvider.generateToken(auth, rUser.get().getUserRole());
            LoginResponse res = new LoginResponse(accessToken, isAdmin, new UserDetailsResponse(rUser.get().getId(), rUser.get().getEmail(), rUser.get().getPassword(), rUser.get().getUserRole(), rUser.get().getEnabled(), rUser.get().getLocked()));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            throw new IllegalAccessException("Email or password invalid");
        }
    }

    public LoginResponse getUserEmail(String email) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        UserDetailsResponse userDetails = new UserDetailsResponse(rUser.getId(), rUser.getEmail(), rUser.getPassword(), rUser.getUserRole(), rUser.getEnabled(), rUser.getLocked());
        boolean isAdmin = rUser.getUserRole().equals(UserRole.ADMIN);
        return new LoginResponse("", isAdmin, userDetails);
    }
}
