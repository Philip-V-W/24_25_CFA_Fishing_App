package cfa.fishing.fishing_store_app.service.contest;

import cfa.fishing.fishing_store_app.dto.request.ContestRequest;
import cfa.fishing.fishing_store_app.dto.response.ContestResponse;
import cfa.fishing.fishing_store_app.dto.response.RegistrationResponse;
import cfa.fishing.fishing_store_app.entity.contest.Contest;
import cfa.fishing.fishing_store_app.entity.contest.ContestRegistration;
import cfa.fishing.fishing_store_app.entity.contest.ContestStatus;
import cfa.fishing.fishing_store_app.entity.contest.RegistrationStatus;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.exception.ResourceNotFoundException;
import cfa.fishing.fishing_store_app.repository.ContestRegistrationRepository;
import cfa.fishing.fishing_store_app.repository.ContestRepository;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    private final ContestRegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ContestResponse createContest(ContestRequest request) {
        Contest contest = Contest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .maxParticipants(request.getMaxParticipants())
                .entryFee(request.getEntryFee())
                .status(ContestStatus.UPCOMING)
                .build();

        return mapToContestResponse(contestRepository.save(contest), null);
    }

    @Transactional
    public RegistrationResponse registerForContest(Long contestId, String userEmail) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate registration
        validateContestRegistration(contest, user);

        ContestRegistration registration = ContestRegistration.builder()
                .contest(contest)
                .user(user)
                .status(RegistrationStatus.PENDING)
                .registrationDate(LocalDateTime.now())
                .build();

        return mapToRegistrationResponse(registrationRepository.save(registration));
    }

    public List<ContestResponse> getUpcomingContests(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        return contestRepository.findUpcomingContests(LocalDateTime.now()).stream()
                .map(contest -> mapToContestResponse(contest, user))
                .collect(Collectors.toList());
    }

    public List<ContestResponse> getOngoingContests(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        return contestRepository.findOngoingContests(LocalDateTime.now()).stream()
                .map(contest -> mapToContestResponse(contest, user))
                .collect(Collectors.toList());
    }

    public List<RegistrationResponse> getUserRegistrations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return registrationRepository.findByUser(user).stream()
                .map(this::mapToRegistrationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContestResponse updateContestStatus(Long contestId, ContestStatus status) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found"));

        validateStatusUpdate(contest, status);
        contest.setStatus(status);

        return mapToContestResponse(contestRepository.save(contest), null);
    }

    private void validateContestRegistration(Contest contest, User user) {
        if (contest.getStatus() != ContestStatus.UPCOMING) {
            throw new IllegalStateException("Contest is not open for registration");
        }

        if (contest.getMaxParticipants() != null) {
            long currentParticipants = registrationRepository.countByContest(contest);
            if (currentParticipants >= contest.getMaxParticipants()) {
                throw new IllegalStateException("Contest is full");
            }
        }

        if (registrationRepository.findByContestAndUser(contest, user).isPresent()) {
            throw new IllegalStateException("Already registered for this contest");
        }
    }

    private void validateStatusUpdate(Contest contest, ContestStatus newStatus) {
        if (contest.getStatus() == ContestStatus.COMPLETED ||
                contest.getStatus() == ContestStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of completed or cancelled contest");
        }
    }

    private ContestResponse mapToContestResponse(Contest contest, User user) {
        Long currentParticipants = registrationRepository.countByContest(contest);
        boolean isRegistered = user != null &&
                registrationRepository.findByContestAndUser(contest, user).isPresent();

        return ContestResponse.builder()
                .id(contest.getId())
                .name(contest.getName())
                .description(contest.getDescription())
                .startDate(contest.getStartDate())
                .endDate(contest.getEndDate())
                .location(contest.getLocation())
                .maxParticipants(contest.getMaxParticipants())
                .entryFee(contest.getEntryFee())
                .status(contest.getStatus())
                .currentParticipants(currentParticipants.intValue())
                .isRegistered(isRegistered)
                .build();
    }

    private RegistrationResponse mapToRegistrationResponse(ContestRegistration registration) {
        return RegistrationResponse.builder()
                .id(registration.getId())
                .participantNumber(registration.getParticipantNumber())
                .contestName(registration.getContest().getName())
                .userEmail(registration.getUser().getEmail())
                .registrationDate(registration.getRegistrationDate())
                .status(registration.getStatus())
                .build();
    }
}