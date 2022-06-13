package rmc.backend.rmc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RCompany {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(
            name = "r_user_id",
            referencedColumnName = "id"
    )

    @MapsId
    private RUser rUser;

    private String name;

    private String description;

    private String website;

    private String address;

    private String type;

    private String companySize;

    private String nation;

//    private List<TechStack> techStacks;

    private String logoImage;

    private float ratingScore;

    private int ratingCount;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public RCompany(RUser rUser) {
        this.rUser = rUser;
    }
}
