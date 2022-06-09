package rmc.backend.rmc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RMember {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(
            name = "r_user_id",
            referencedColumnName = "id"
    )

    @MapsId
    private RUser rUser;

    private String nickname;

    private String avatar;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rating> rating;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comments> comment;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> report;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public RMember(RUser rUser) {
        this.rUser = rUser;
    }
}
