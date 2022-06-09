package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutCompanyInfoRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 6, max = 50, message = "Address must be between 6 and 50 characters")
    private String address;

    private String type;

    private String website;

    @NotBlank(message = "Name is required")
    @Size(min = 6, max = 50, message = "Name must be between 6 and 50 characters")
    private String description;

    private String logoImage;
}
