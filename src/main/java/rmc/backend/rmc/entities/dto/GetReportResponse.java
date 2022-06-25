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

    private String ratingId;

    private String ratingBy;

    private String ratingContent;

    private LocalDateTime dateReport;
}
