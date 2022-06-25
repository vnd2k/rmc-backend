package rmc.backend.rmc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RAdmin {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(
            name = "r_user_id",
            referencedColumnName = "id"
    )

    @MapsId
    private RUser rUser;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public RAdmin(RUser rUser) {
        this.rUser = rUser;
    }
}
