package th.ac.ku.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;

    private String name;

    @Column(name = "user_role")
    private String role;
    private Instant createdAt;

}
