package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Service.RequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService requirementService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRequirements() {
        return ResponseEntity.ok(requirementService.getAllRequirements());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRequirementById(@PathVariable Integer id) {
        return ResponseEntity.ok(requirementService.getRequirementById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRequirement(@Valid @RequestBody RequirementRequestDTO requirementRequestDTO) {
        requirementService.addRequirement(requirementRequestDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Requirement added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequirement(@PathVariable Integer id, @Valid @RequestBody RequirementRequestDTO requirementRequestDTO) {
        requirementService.updateRequirement(id, requirementRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Requirement updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequirement(@PathVariable Integer id) {
        requirementService.deleteRequirement(id);
        return ResponseEntity.ok(new ApiResponse("Requirement deleted successfully"));
    }
}
