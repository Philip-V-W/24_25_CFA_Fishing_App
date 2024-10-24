package cfa.fishing.fishing_store_app.dto.response;

import cfa.fishing.fishing_store_app.entity.permit.PermitStatus;
import cfa.fishing.fishing_store_app.entity.permit.PermitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitResponse {
    private Long id;
    private String permitNumber;
    private String userEmail;
    private PermitType permitType;
    private LocalDate startDate;
    private LocalDate endDate;
    private PermitStatus status;
    private BigDecimal price;
    private String notes;
}