package cfa.fishing.fishing_store_app.controller.api;

import cfa.fishing.fishing_store_app.dto.request.ContestRequest;
import cfa.fishing.fishing_store_app.dto.response.ContestResponse;
import cfa.fishing.fishing_store_app.dto.response.RegistrationResponse;
import cfa.fishing.fishing_store_app.entity.contest.ContestStatus;
import cfa.fishing.fishing_store_app.service.contest.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @PostMapping
    public ResponseEntity<ContestResponse> createContest(@RequestBody ContestRequest request) {
        return ResponseEntity.ok(contestService.createContest(request));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ContestResponse>> getUpcomingContests(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contestService.getUpcomingContests(userDetails.getUsername()));
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<ContestResponse>> getOngoingContests(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contestService.getOngoingContests(userDetails.getUsername()));
    }

    @PostMapping("/{contestId}/register")
    public ResponseEntity<RegistrationResponse> registerForContest(
            @PathVariable Long contestId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contestService.registerForContest(contestId, userDetails.getUsername()));
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<RegistrationResponse>> getUserRegistrations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contestService.getUserRegistrations(userDetails.getUsername()));
    }

    @PatchMapping("/{contestId}/status")
    public ResponseEntity<ContestResponse> updateContestStatus(
            @PathVariable Long contestId,
            @RequestParam ContestStatus status) {
        return ResponseEntity.ok(contestService.updateContestStatus(contestId, status));
    }
}