package rmc.backend.rmc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LikeRating implements Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "rating_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Rating rating;

    private String memberId;
}
