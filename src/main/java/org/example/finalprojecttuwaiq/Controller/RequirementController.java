package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.RequirementRequestDTO;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.DraftRequirementService;
import org.example.finalprojecttuwaiq.Service.RequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/draft/project/{project_id}")
    public ResponseEntity<?> getAllDraftRequirement(@PathVariable Integer project_id){
        return ResponseEntity.ok(draftRequirementService.getDraftRequirementByProjectId(project_id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRequirementById(@PathVariable Integer id) {
        return ResponseEntity.ok(requirementService.getRequirementById(id));
    }


    @PostMapping("/add")
    public ResponseEntity<?> addRequirement(@AuthenticationPrincipal User user, @RequestBody @Valid RequirementRequestDTO requirementRequestDTO){
        requirementService.addRequirement(user.getId(),requirementRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Requirement Added Successfully"));
    }


    @GetMapping("/by-project/{project_id}")
    public ResponseEntity<?> getRequirementsByProjectId(@AuthenticationPrincipal User user,@PathVariable Integer project_id){
        return ResponseEntity.ok(requirementService.getRequirementsByProjectId(user.getId(), project_id));
    }

    @PostMapping("/generate/{project_id}")
    public ResponseEntity<?> generateRequirements(@AuthenticationPrincipal User user,@PathVariable Integer project_id) {
        requirementService.extractRequirements(user.getId(),project_id);
        return ResponseEntity.status(201).body("Requirements Generated and Drafted Successfully");
    }


    @GetMapping("/draft/get/{draft_id}")
    public ResponseEntity<?> getDraftById(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        return ResponseEntity.ok(draftRequirementService.getDraftById(user.getId(), draft_id));
    }
    @PostMapping("/draft/accept/{draft_id}")
    public ResponseEntity<?> acceptDraft(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        requirementService.acceptDraft(user.getId(),draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft accepted!"));
    }
    @DeleteMapping("/draft/reject/{draft_id}")
    public ResponseEntity<?> rejectDraft(@AuthenticationPrincipal User user,@PathVariable Integer draft_id){
        draftRequirementService.rejectDraft(user.getId(), draft_id);
        return ResponseEntity.ok(new ApiResponse("Draft rejected!"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequirement(@AuthenticationPrincipal User user,@PathVariable Integer id, @Valid @RequestBody RequirementRequestDTO requirementRequestDTO) {
        requirementService.updateRequirement(user.getId(),id, requirementRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Requirement updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequirement(@AuthenticationPrincipal User user,@PathVariable Integer id) {
        requirementService.deleteRequirement(user.getId(),id);
        return ResponseEntity.ok(new ApiResponse("Requirement deleted successfully"));
    }
}
