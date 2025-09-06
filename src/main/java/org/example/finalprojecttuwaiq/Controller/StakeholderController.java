package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.example.finalprojecttuwaiq.Service.StakeholderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.finalprojecttuwaiq.DTO.StakeholderRequestDTO;

import java.util.List;

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
    public ResponseEntity<?> addStakeholder(@Valid @RequestBody StakeholderRequestDTO stakeholderRequestDTO) {
        stakeholderService.registerStakeholder(stakeholderRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Stakeholder Registered successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStakeholder(@PathVariable Integer id, @Valid @RequestBody StakeholderRequestDTO stakeholderRequestDTO) {
        stakeholderService.updateStakeholder(id, stakeholderRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Stakeholder updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStakeholder(@PathVariable Integer id) {
        stakeholderService.deleteStakeholder(id);
        return ResponseEntity.ok(new ApiResponse("Stakeholder deleted successfully"));
    }

    @GetMapping("/projects/{stakeholder_id}")
    public ResponseEntity<?> getProjectsByStakeholderId(@PathVariable Integer stakeholder_id){
        return ResponseEntity.ok(stakeholderService.getProjectsByStakeholderId(stakeholder_id));
    }
}
