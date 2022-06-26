package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReportResponse {
    private String reportId;

    private String reporterId;

    private String reporterAvatar;

    private String ratingId;

    private String reporter;

    private String reason;

    private LocalDateTime dateReport;
}
