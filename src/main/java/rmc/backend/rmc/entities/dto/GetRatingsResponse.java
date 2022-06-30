package rmc.backend.rmc.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRatingsResponse {
    private String id;

    private String companyId;

    private int ratingPoint;

    private String raterName;

    private String positivePoint;

    private String pointToImprove;

    private int likeCount;

    private int dislikeCount;

    private boolean liked;

    private boolean unliked;

    private boolean myRating;

    private boolean reported;

    private String companyName;

    private int totalPage;

    private int reportedCount;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime createdAt;
}
