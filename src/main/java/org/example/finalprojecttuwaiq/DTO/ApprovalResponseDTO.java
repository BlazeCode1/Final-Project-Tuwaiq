package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponseDTO {

    @NotNull(message = "Approval ID cannot be null")
   private Integer approvalId;

    @NotNull(message = "Stakeholder ID cannot be null")
   private Integer stakeholderId;

   private String comment;
}
