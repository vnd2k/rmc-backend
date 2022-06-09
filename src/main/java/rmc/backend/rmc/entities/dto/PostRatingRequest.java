package rmc.backend.rmc.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRatingRequest {
    @NotBlank(message = "Rating title is required")
    @Size(max = 1000, message = "Rating title must be between 0 and 100 characters")
    private String ratingTitle;

    @NotBlank(message = "Positive point is required")
    @Size(max = 1000, message = "Positive point must be between 0 and 100 characters")
    private String positivePoint;

    @NotBlank(message = "Points to improve is required")
    @Size(max = 1000, message = "Points to improve must be between 0 and 100 characters")
    private String pointsToImprove;

    @NotNull
    @Range(min = 1, max = 5)
    private int ratingPoint;

    @NotBlank
    private String companyId;
}
