package cfa.fishing.fishing_store_app.entity.contest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contests")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer maxParticipants;
    private BigDecimal entryFee;

    @Enumerated(EnumType.STRING)
    private ContestStatus status = ContestStatus.UPCOMING;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<ContestRegistration> registrations = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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