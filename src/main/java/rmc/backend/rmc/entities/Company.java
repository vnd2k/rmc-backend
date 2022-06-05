package rmc.backend.rmc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
    @Id
    private String id;

    private String email;

    private String password;

    private String name;

    private String description;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @MapsId
    private RUser rUser;

    public Company(RUser rUser){
        this.rUser = rUser;
    }
}
