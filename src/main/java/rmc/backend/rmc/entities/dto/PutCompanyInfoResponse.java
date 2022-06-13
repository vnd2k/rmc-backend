package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutCompanyInfoResponse {
    private String name;

    private String address;

    private String type;

    private String website;

    private String description;

    private String companySize;

    private String nation;

    private String logoImage;
}
