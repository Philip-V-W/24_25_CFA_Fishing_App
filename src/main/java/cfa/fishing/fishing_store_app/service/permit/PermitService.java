package cfa.fishing.fishing_store_app.service.permit;

import cfa.fishing.fishing_store_app.dto.request.PermitRequest;
import cfa.fishing.fishing_store_app.dto.response.PermitResponse;
import cfa.fishing.fishing_store_app.entity.permit.FishingPermit;
import cfa.fishing.fishing_store_app.entity.permit.PermitStatus;
import cfa.fishing.fishing_store_app.entity.permit.PermitType;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.exception.ResourceNotFoundException;
import cfa.fishing.fishing_store_app.repository.PermitRepository;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermitService {

    private final PermitRepository permitRepository;
    private final UserRepository userRepository;

    @Transactional
    public PermitResponse createPermit(String userEmail, PermitRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FishingPermit permit = new FishingPermit();
        permit.setUser(user);
        permit.setPermitType(request.getPermitType());
        permit.setStartDate(request.getStartDate());
        permit.setEndDate(calculateEndDate(request.getStartDate(), request.getPermitType()));
        permit.setPrice(calculatePrice(request.getPermitType()));
        permit.setNotes(request.getNotes());
        permit.setStatus(PermitStatus.PENDING);

        return mapToPermitResponse(permitRepository.save(permit));
    }

    @Transactional
    public PermitResponse approvePermit(Long permitId) {
        FishingPermit permit = permitRepository.findById(permitId)
                .orElseThrow(() -> new ResourceNotFoundException("Permit not found"));

        if (permit.getStatus() != PermitStatus.PENDING) {
            throw new IllegalStateException("Permit is not in PENDING status");
        }

        permit.setStatus(PermitStatus.APPROVED);
        return mapToPermitResponse(permitRepository.save(permit));
    }

    @Transactional
    public PermitResponse rejectPermit(Long permitId, String reason) {
        FishingPermit permit = permitRepository.findById(permitId)
                .orElseThrow(() -> new ResourceNotFoundException("Permit not found"));

        if (permit.getStatus() != PermitStatus.PENDING) {
            throw new IllegalStateException("Permit is not in PENDING status");
        }

        permit.setStatus(PermitStatus.REJECTED);
        permit.setNotes(reason);
        return mapToPermitResponse(permitRepository.save(permit));
    }

    public List<PermitResponse> getUserPermits(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return permitRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToPermitResponse)
                .collect(Collectors.toList());
    }

    public PermitResponse getPermit(Long permitId) {
        return permitRepository.findById(permitId)
                .map(this::mapToPermitResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Permit not found"));
    }

    private LocalDate calculateEndDate(LocalDate startDate, PermitType type) {
        return switch (type) {
            case DAILY -> startDate;
            case WEEKLY -> startDate.plusDays(7);
            case MONTHLY -> startDate.plusMonths(1);
            case ANNUAL -> startDate.plusYears(1);
        };
    }

    private BigDecimal calculatePrice(PermitType type) {
        return switch (type) {
            case DAILY -> new BigDecimal("15.00");
            case WEEKLY -> new BigDecimal("45.00");
            case MONTHLY -> new BigDecimal("100.00");
            case ANNUAL -> new BigDecimal("250.00");
        };
    }

    private PermitResponse mapToPermitResponse(FishingPermit permit) {
        PermitResponse response = new PermitResponse();
        response.setId(permit.getId());
        response.setPermitNumber(permit.getPermitNumber());
        response.setUserEmail(permit.getUser().getEmail());
        response.setPermitType(permit.getPermitType());
        response.setStartDate(permit.getStartDate());
        response.setEndDate(permit.getEndDate());
        response.setStatus(permit.getStatus());
        response.setPrice(permit.getPrice());
        response.setNotes(permit.getNotes());
        return response;
    }
}