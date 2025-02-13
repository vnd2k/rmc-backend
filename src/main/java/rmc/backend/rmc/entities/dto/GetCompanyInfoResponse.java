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
    private String id;

    private String name;

    private String address;

    private String type;

    private String website;

    private String description;

    private String companySize;

    private String nation;

    private String logoImage;

    private float ratingScore;

    private int ratingCount;

    private List<Rating> ratings;

    private int oneStarCount;

    private int twoStarCount;

    private int threeStarCount;

    private int fourStarCount;

    private int fiveStarCount;

    private boolean verified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
