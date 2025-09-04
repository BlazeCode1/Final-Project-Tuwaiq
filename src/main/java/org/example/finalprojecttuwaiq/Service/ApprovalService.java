package org.example.finalprojecttuwaiq.Service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.ApprovalRequestDTO;
import org.example.finalprojecttuwaiq.Model.Approval;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Document;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.example.finalprojecttuwaiq.Repository.ApprovalRepository;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.DocumentRepository;
import org.example.finalprojecttuwaiq.Repository.StakeholderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final StakeholderRepository stakeholderRepository;
    private final DocumentRepository documentRepository;
    private final BARepository baRepository;
    private final MailService mailService;

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public Approval getApprovalById(Integer id) {
        return approvalRepository.findById(id).orElseThrow(() -> new ApiException("Approval with id " + id + " not found"));
    }



    public void sendApproval(Integer baId, ApprovalRequestDTO approvalRequestDTO) {

        BA ba = baRepository.findById(baId)
                .orElseThrow(() -> new ApiException("Business Analyst with ID " + baId + " not found"));
        Stakeholder stakeholder = stakeholderRepository.findById(approvalRequestDTO.getStakeholderId())
                .orElseThrow(() -> new ApiException("Stakeholder with ID " + approvalRequestDTO.getStakeholderId() + " not found"));
        Document document = documentRepository.findById(approvalRequestDTO.getDocumentId())
                .orElseThrow(() -> new ApiException("Document with ID " + approvalRequestDTO.getDocumentId() + " not found"));

        Approval approval = new Approval();
        approval.setStatus("PENDING");
        approval.setComments(approvalRequestDTO.getComments());
        approval.setReviewedAt(LocalDateTime.now());
        approval.setStakeholder(stakeholder);
        approval.setDocument(document);
        mailService.sendApprovalRequestEmail(stakeholder,document,ba);
        approvalRepository.save(approval);
    }

    public void updateApproval(Integer id, ApprovalRequestDTO approvalRequestDTO) {
        Approval existingApproval = approvalRepository.findById(id).orElseThrow(() -> new ApiException("Approval with id " + id + " not found"));
        Stakeholder stakeholder = stakeholderRepository.findById(approvalRequestDTO.getStakeholderId())
                .orElseThrow(() -> new ApiException("Stakeholder with ID " + approvalRequestDTO.getStakeholderId() + " not found"));
        Document document = documentRepository.findById(approvalRequestDTO.getDocumentId())
                .orElseThrow(() -> new ApiException("Document with ID " + approvalRequestDTO.getDocumentId() + " not found"));

        existingApproval.setStatus(approvalRequestDTO.getStatus());
        existingApproval.setComments(approvalRequestDTO.getComments());
        existingApproval.setReviewedAt(LocalDateTime.now());
        existingApproval.setStakeholder(stakeholder);
        existingApproval.setDocument(document);
        approvalRepository.save(existingApproval);
    }

    public void deleteApproval(Integer id) {
        Approval approval = approvalRepository.findById(id).orElseThrow(() -> new ApiException("Approval with id " + id + " not found"));
        approvalRepository.delete(approval);
    }
}
