package cfa.fishing.fishing_store_app.dto.request;

import cfa.fishing.fishing_store_app.entity.permit.PermitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitRequest {
    private PermitType permitType;
    private LocalDate startDate;
    private String notes;
}