package rmc.backend.rmc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne
    @JoinColumn(nullable = false, name = "company_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RCompany company;

    @ManyToOne
    @JoinColumn(nullable = false, name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RMember member;

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Report> report = new ArrayList<>();

    @Lob
    private String positivePoint;

    @Lob
    private String pointsToImprove;

    private int ratingPoint;

    private int reactionCount;

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LikeRating> likes = new ArrayList<>();

    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UnlikeRating> unlike = new ArrayList<>();

    private LocalDateTime createdAt;
}
