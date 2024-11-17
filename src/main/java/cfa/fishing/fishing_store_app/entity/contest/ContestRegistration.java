package cfa.fishing.fishing_store_app.entity.contest;

import cfa.fishing.fishing_store_app.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contest_registrations")
public class ContestRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    private String participantNumber;

    @PrePersist
    protected void onCreate() {
        if (participantNumber == null) {
            // Generate participant number: CONT-YYYY-RandomNumber
            String year = String.valueOf(LocalDateTime.now().getYear());
            String random = String.format("%04d", (int) (Math.random() * 10000));
            this.participantNumber = "CONT-" + year + "-" + random;
        }
    }
}