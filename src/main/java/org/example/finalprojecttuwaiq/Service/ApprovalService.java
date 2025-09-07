package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.DTO.ApprovalRequestDTO;
import org.example.finalprojecttuwaiq.DTO.ApprovalResponseDTO;
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
import java.util.Objects;

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

    public List<Approval> getPendingApprovalsByStakeholderId(Integer stakeholder_id){
        Stakeholder stakeholder = stakeholderRepository.findById(stakeholder_id)
                .orElseThrow(() -> new ApiException("StakeHolder Not Found"));
        if (!stakeholder.getUser().getRole().equalsIgnoreCase("Stakeholder"))
            throw new ApiException("Unauthorized");
        return approvalRepository.getApprovalsByStakeholderAndStatus(stakeholder,"PENDING");
    }



    public void sendApproval(Integer baId, ApprovalRequestDTO approvalRequestDTO) {

        BA ba = baRepository.findById(baId)
                .orElseThrow(() -> new ApiException("Business Analyst with ID " + baId + " not found"));

        if (!ba.getIsSubscribed()){
            throw new ApiException("Unauthorized, you are not subscribed");
        }

        Stakeholder stakeholder = stakeholderRepository.findById(approvalRequestDTO.getStakeholderId())
                .orElseThrow(() -> new ApiException("Stakeholder with ID " + approvalRequestDTO.getStakeholderId() + " not found"));
        Document document = documentRepository.findById(approvalRequestDTO.getDocumentId())
                .orElseThrow(() -> new ApiException("Document with ID " + approvalRequestDTO.getDocumentId() + " not found"));
        if (!ba.getUser().getRole().equalsIgnoreCase("BA"))
            throw new ApiException("Unauthorized");
        Approval approval = new Approval();
        approval.setStatus("PENDING");
        approval.setBa(ba);
        approval.setComments(approvalRequestDTO.getComments());
        approval.setReviewedAt(LocalDateTime.now());
        approval.setStakeholder(stakeholder);
        approval.setDocument(document);
        approvalRepository.save(approval);
        mailService.sendApprovalRequestEmail(stakeholder,document,ba);
    }

    public void acceptApproval(Integer stakeholder_id,ApprovalResponseDTO approvalResponseDTO){
        Approval approval = approvalRepository.findById(approvalResponseDTO.getApprovalId())
                .orElseThrow(() -> new ApiException("Approval with ID "+approvalResponseDTO.getApprovalId()+" not found"));


        Stakeholder stakeholder = stakeholderRepository.findById(stakeholder_id)
                .orElseThrow(()-> new ApiException("Approval with ID "+stakeholder_id+" not found"));

        if (!Objects.equals(approval.getStakeholder().getId(), stakeholder.getId())){
            throw new ApiException("This stakeholder is not authorized to approve this request.");
        }

        BA ba = approval.getBa();
        if (ba == null){
            throw new ApiException("Approval requester (BA) is missing or has no email.");
        }

        approval.setStatus("APPROVED");
        approval.setReviewedAt(LocalDateTime.now());
        approval.setComments(approvalResponseDTO.getComment());
        approval.getDocument().getProject().setStatus("Validation");
        approvalRepository.save(approval);
        mailService.sendApprovedEmail(ba,approval.getDocument(),stakeholder, approval.getComments());
    }

    public void rejectApproval(Integer stakeholder_id,ApprovalResponseDTO approvalResponseDTO){
        Approval approval = approvalRepository.findById(approvalResponseDTO.getApprovalId())
                .orElseThrow(() -> new ApiException("Approval with ID "+approvalResponseDTO.getApprovalId()+" not found"));

        Stakeholder stakeholder = stakeholderRepository.findById(stakeholder_id)
                .orElseThrow(()-> new ApiException("Approval with ID "+stakeholder_id+" not found"));

        if (!Objects.equals(approval.getStakeholder().getId(), stakeholder.getId())){
            throw new ApiException("This stakeholder is not authorized to approve this request.");
        }

        BA ba = approval.getBa();
        if (ba == null){
            throw new ApiException("Approval requester (BA) is missing or has no email.");
        }

        approval.setStatus("REJECTED");
        approval.setReviewedAt(LocalDateTime.now());
        approval.setBa(ba);
        approval.setComments(approvalResponseDTO.getComment());
        approvalRepository.save(approval);
        mailService.sendRejectedEmail(ba,approval.getDocument(),stakeholder, approval.getComments());
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
