package org.example.finalprojecttuwaiq.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.example.finalprojecttuwaiq.DTO.ApprovalRequestDTO;
import org.example.finalprojecttuwaiq.DTO.ApprovalResponseDTO;
import org.example.finalprojecttuwaiq.Model.Approval;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;


    @PostMapping("/send")
    public ResponseEntity<?> sendApprovalRequest(@AuthenticationPrincipal User user, @RequestBody @Valid ApprovalRequestDTO approvalRequestDTO) {
        approvalService.sendApproval(user.getId(), approvalRequestDTO);
        return ResponseEntity.ok().body(new ApiResponse("Request Sent Successfully"));
    }
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingApprovalsByStakeholder(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(approvalService.getPendingApprovalsByStakeholderId(user.getId()));
    }
    @PostMapping("/approve")
    public ResponseEntity<?> acceptApproval(@AuthenticationPrincipal User user,@RequestBody @Valid ApprovalResponseDTO approvalResponseDTO) {
        approvalService.acceptApproval(user.getId(),approvalResponseDTO);
        return ResponseEntity.ok().body(new ApiResponse("Document approved"));
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectApproval(@AuthenticationPrincipal User user,@RequestBody @Valid ApprovalResponseDTO approvalResponseDTO) {
        approvalService.rejectApproval(user.getId(),approvalResponseDTO);
        return ResponseEntity.ok().body(new ApiResponse("Document rejected"));
    }
    @GetMapping("/get")
    public ResponseEntity<?> getAllApprovals() {
        return ResponseEntity.ok(approvalService.getAllApprovals());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getApprovalById(@PathVariable Integer id) {
        return ResponseEntity.ok(approvalService.getApprovalById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateApproval(@PathVariable Integer id, @Valid @RequestBody ApprovalRequestDTO approvalRequestDTO) {
        approvalService.updateApproval(id, approvalRequestDTO);
        return ResponseEntity.ok(new ApiResponse("Approval updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteApproval(@PathVariable Integer id) {
        approvalService.deleteApproval(id);
        return ResponseEntity.ok(new ApiResponse("Approval deleted successfully"));
    }




}
