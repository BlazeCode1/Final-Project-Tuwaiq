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

    @NotNull(message = "Stakeholder ID cannot be null")
    private Integer stakeholderId;

    @NotNull(message = "Document ID cannot be null")
    private Integer documentId;
}
