package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutCompanyByAdminRequest {
    private String name;

    private String address;

    private String type;

    private String website;

    private String nation;

    private String companySize;

    private String description;

    private boolean verified;
}
