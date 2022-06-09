package rmc.backend.rmc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "company_id")
    @JsonIgnore
    private RCompany company;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "member_id")
    @JsonIgnore
    private RMember member;

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Report> report = new ArrayList<>();

    private String ratingTitle;

    private String positivePoint;

    private String pointsToImprove;

    private int ratingPoint;

    private int likeCount;

    private int unlikeCount;

    private LocalDateTime createdAt;
}
