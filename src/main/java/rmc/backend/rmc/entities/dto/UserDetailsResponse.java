package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmc.backend.rmc.security.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private String id;
    private String email;
    private String password;
    private String nickname;
    private String avatar;
    private UserRole role;
    private boolean enabled;
    private boolean locked;
}
