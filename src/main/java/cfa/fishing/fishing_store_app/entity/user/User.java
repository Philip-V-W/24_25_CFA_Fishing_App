package cfa.fishing.fishing_store_app.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter // Generates getters
@Setter // Generates setters
@NoArgsConstructor // Generates empty constructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor with required fields
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.CUSTOMER;
        this.active = true;
    }

    // Full constructor except id and timestamps
    public User(String email, String password, String firstName,
                String lastName, String address, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = Role.CUSTOMER;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}