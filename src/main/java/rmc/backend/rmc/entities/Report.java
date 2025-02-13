package rmc.backend.rmc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Report {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "rating_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Rating rating;

    @ManyToOne
    @JoinColumn(nullable = false, name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RMember member;

    private String reason;

    private String description;

    private LocalDateTime createdAt;
}
