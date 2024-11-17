package cfa.fishing.fishing_store_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime registrationDate;

    @Builder.Default
    private List<AddressResponse> addresses = List.of();

    @Builder.Default
    private List<OrderSummaryResponse> recentOrders = List.of();

    @Builder.Default
    private List<PermitSummaryResponse> activePermits = List.of();

    @Builder.Default
    private List<ContestRegistrationResponse> upcomingContests = List.of();
}