package rmc.backend.rmc.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListJobResponse {
    private String id;

    private String title;

    private String description;

    private String logo;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime createdAt;
}
