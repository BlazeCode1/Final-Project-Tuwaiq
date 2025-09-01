package org.example.finalprojecttuwaiq.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryRequestDTO {
    @NotEmpty(message = "'As a' field cannot be Empty")
    @Size(max = 255, message = "'As a' field cannot exceed 255 characters")
    private String asA;

    @NotEmpty(message = "'I want' field cannot be blank")
    @Size(max = 255, message = "'I want' field cannot exceed 255 characters")
    private String iWant;

    @NotEmpty(message = "'So that' field cannot be blank")
    @Size(max = 255, message = "'So that' field cannot exceed 255 characters")
    private String soThat;

    @Pattern(regexp = "^(Must|Should|Could|Wont|High|Medium|Low)$", message = "Priority must be one of Must, Should, Could, Wont, High, Medium, Low")
    private String priority;

    @NotEmpty(message = "Acceptance criteria cannot be blank")
    private String acceptanceCriteria;

    @Pattern(regexp = "^(Draft|Ready|InProgress|Done|Blocked)$", message = "Status must be one of Draft, Ready, InProgress, Done, Blocked")
    private String status;

    @NotNull(message = "Requirement ID cannot be null")
    private Integer requirementId;
}
