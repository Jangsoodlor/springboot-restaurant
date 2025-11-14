package th.ac.ku.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;
import th.ac.ku.restaurant.security.AttributeEncryptor;

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

    @Convert(converter = AttributeEncryptor.class)
    private String name;

    @Column(name = "user_role")
    private String role;
    private Instant createdAt;

}
