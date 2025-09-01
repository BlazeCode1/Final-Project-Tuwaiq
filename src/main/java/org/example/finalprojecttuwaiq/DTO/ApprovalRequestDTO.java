package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestDTO {
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED)$", message = "Status must be one of PENDING, APPROVED, REJECTED")
    private String status;

    @Min(value = 0, message = "Approved version must be non-negative")
    private int approvedVersion;

    @Size(max = 2000, message = "Comments cannot exceed 2000 characters")
    private String comments;

    @NotNull(message = "Stakeholder ID cannot be null")
    private Integer stakeholderId;

    @NotNull(message = "Document ID cannot be null")
    private Integer documentId;
}
