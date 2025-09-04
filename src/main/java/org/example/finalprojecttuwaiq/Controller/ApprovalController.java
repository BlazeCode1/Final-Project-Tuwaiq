package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.ApprovalRequestDTO;
import org.example.finalprojecttuwaiq.DTO.ApprovalResponseDTO;
import org.example.finalprojecttuwaiq.Model.Approval;
import org.example.finalprojecttuwaiq.Service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/get")
    public ResponseEntity<List<Approval>> getAllApprovals() {
        return ResponseEntity.ok(approvalService.getAllApprovals());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Approval> getApprovalById(@PathVariable Integer id) {
        return ResponseEntity.ok(approvalService.getApprovalById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateApproval(@PathVariable Integer id, @Valid @RequestBody ApprovalRequestDTO approvalRequestDTO) {
        approvalService.updateApproval(id, approvalRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Approval updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteApproval(@PathVariable Integer id) {
        approvalService.deleteApproval(id);
        return ResponseEntity.ok(new ApiResponse("Approval deleted successfully"));
    }

    @PostMapping("/{baId}/send/")
    public ResponseEntity<?> sendApprovalRequest(@PathVariable Integer baId, @RequestBody @Valid ApprovalRequestDTO approvalRequestDTO) {
        approvalService.sendApproval(baId, approvalRequestDTO);
        return ResponseEntity.ok().body(new ApiResponse("Request Sent Successfully"));
    }


    @PostMapping("/approve")
    public ResponseEntity<?> acceptApproval(@RequestBody @Valid ApprovalResponseDTO approvalResponseDTO) {
        approvalService.acceptApproval(approvalResponseDTO);
        return ResponseEntity.ok().body(new ApiResponse("Document approved"));
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectApproval(@RequestBody @Valid ApprovalResponseDTO approvalResponseDTO) {
        approvalService.rejectApproval(approvalResponseDTO);
        return ResponseEntity.ok().body(new ApiResponse("Document rejected"));
    }

    @GetMapping("/pending/{stakeholder_id}")
    public ResponseEntity<?> getPendingApprovalsByStakeholder(@PathVariable Integer stakeholder_id){
        return ResponseEntity.ok(approvalService.getPendingApprovalsByStakeholderId(stakeholder_id));

    }
}
