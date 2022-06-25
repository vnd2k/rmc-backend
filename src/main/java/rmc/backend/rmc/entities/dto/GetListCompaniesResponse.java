package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListCompaniesResponse {
    private String id;

    private String name;

    private String address;

    private String type;

    private String website;

    private String description;

    private String companySize;

    private String nation;

    private String logoImage;

    private boolean verified;

    private float ratingScore;

    private int ratingCount;
}
