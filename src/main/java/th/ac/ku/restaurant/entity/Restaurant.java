package th.ac.ku.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Restaurant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;
    private double rating;
    private String location;
    private Instant createdAt;
}
