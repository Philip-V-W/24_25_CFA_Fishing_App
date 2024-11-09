package cfa.fishing.fishing_store_app.dto.response;

import cfa.fishing.fishing_store_app.entity.contest.ContestStatus;
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
public class ContestResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer maxParticipants;
    private BigDecimal entryFee;
    private ContestStatus status;
    private Integer currentParticipants;
    private Boolean isRegistered;
}