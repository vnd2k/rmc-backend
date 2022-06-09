package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmc.backend.rmc.entities.Rating;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCompanyInfoResponse {
    private String name;

    private String address;

    private String description;

    private String website;

    private String type;

    private String logoImage;

    private float ratingScore;

    private int ratingCount;

    private List<Rating> ratings;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
