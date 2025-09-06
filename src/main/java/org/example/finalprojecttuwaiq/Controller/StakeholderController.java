package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.StakeholderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.example.finalprojecttuwaiq.DTO.StakeholderRequestDTO;


@RestController
@RequestMapping("/api/v1/stakeholder")
@RequiredArgsConstructor
public class StakeholderController {

    private final StakeholderService stakeholderService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllStakeholders() {
        return ResponseEntity.ok(stakeholderService.getAllStakeholders());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStakeholderById(@PathVariable Integer id) {
        return ResponseEntity.ok(stakeholderService.getStakeholderById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody StakeholderRequestDTO stakeholderRequestDTO) {
        stakeholderService.registerStakeholder(stakeholderRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Stakeholder Registered successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStakeholder(@AuthenticationPrincipal User user, @Valid @RequestBody StakeholderRequestDTO stakeholderRequestDTO) {
        stakeholderService.updateStakeholder(user.getId(), stakeholderRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Stakeholder updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStakeholder(@AuthenticationPrincipal User user) {
        stakeholderService.deleteStakeholder(user.getId());
        return ResponseEntity.ok(new ApiResponse("Stakeholder deleted successfully"));
    }

    @GetMapping("/projects/{stakeholder_id}")
    public ResponseEntity<?> getProjectsByStakeholderId(@PathVariable Integer stakeholder_id){
        return ResponseEntity.ok(stakeholderService.getProjectsByStakeholderId(stakeholder_id));
    }
}
