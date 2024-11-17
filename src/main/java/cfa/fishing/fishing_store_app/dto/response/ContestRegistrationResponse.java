package cfa.fishing.fishing_store_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestRegistrationResponse {
    private Long id;
    private Long contestId;
    private String contestName;
    private LocalDateTime contestDate;
    private String location;
    private String registrationNumber;
    private LocalDateTime registrationDate;
    private BigDecimal entryFee;
    private String status;
}