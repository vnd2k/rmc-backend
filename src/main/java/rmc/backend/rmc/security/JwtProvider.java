package rmc.backend.rmc.security;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import rmc.backend.rmc.entities.RUser;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtProvider {
    @Async
    public String generateToken(Authentication authentication, UserRole role) {
        RUser principal = (RUser) authentication.getPrincipal();
        String SECRET = "ThisIsMyLittleSecretThisIsMyLittleSecretThisIsMyLittleSecret";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role.name());

        return Jwts.builder()
                .setSubject(principal.getEmail())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS256,
                        SECRET.getBytes())
                .compact();
    }
}
