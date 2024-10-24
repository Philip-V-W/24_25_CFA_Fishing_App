package cfa.fishing.fishing_store_app.dto.response;

import cfa.fishing.fishing_store_app.entity.contest.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private Long id;
    private String participantNumber;
    private String contestName;
    private String userEmail;
    private LocalDateTime registrationDate;
    private RegistrationStatus status;
}