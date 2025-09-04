package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Service.DraftRequirementService;
import org.example.finalprojecttuwaiq.Service.RequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService requirementService;
    private final DraftRequirementService draftRequirementService;
    @GetMapping("/get")
    public ResponseEntity<?> getAllRequirements() {
        return ResponseEntity.ok(requirementService.getAllRequirements());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRequirementById(@PathVariable Integer id) {
        return ResponseEntity.ok(requirementService.getRequirementById(id));
    }
    @GetMapping("/by-project/{project_id}")
    public ResponseEntity<?> getRequirementsByProjectId(@PathVariable Integer project_id){
        return ResponseEntity.ok(requirementService.getRequirementsByProjectId(project_id));
    }

    @PostMapping("/generate/{project_id}")
    public ResponseEntity<?> generateRequirements(@PathVariable Integer project_id) {
        requirementService.extractRequirements(project_id);
        return ResponseEntity.status(201).body("Requirements Generated and Drafted Successfully");
    }

    @GetMapping("/draft/project/{project_id}")
    public ResponseEntity<?> getAllDraftRequirement(@PathVariable Integer project_id){
        return ResponseEntity.ok(draftRequirementService.getDraftRequirementByProjectId(project_id));
    }
    @GetMapping("/draft/get/{draft_id}")
    public ResponseEntity<?> getDraftById(@PathVariable Integer draft_id){
        return ResponseEntity.ok(draftRequirementService.getDraftById(draft_id));
    }
    @PostMapping("/draft/accept/{draft_id}")
    public ResponseEntity<?> acceptDraft(@PathVariable Integer draft_id){
        requirementService.acceptDraft(draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft accepted!"));
    }
    @DeleteMapping("/draft/reject/{draft_id}")
    public ResponseEntity<?> rejectDraft(@PathVariable Integer draft_id){
        draftRequirementService.rejectDraft(draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft rejected!"));
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

    @PostMapping("/wireframe/{requirementId}")
    public ResponseEntity<?> generateFromRequirement(@PathVariable Integer requirementId) {
        String mermaid = requirementService.generateUiLayoutJson(requirementId);
        return ResponseEntity.ok(new ApiResponse(mermaid));
    }
}
