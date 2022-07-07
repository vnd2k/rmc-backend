package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListCvResponse {
    private String id;

    private String cvUrl;

    private String memberId;

    private String email;

    private String jobId;
}
