package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutMemberInfoResponse {
    private String nickname;

    private String avatar;
}
