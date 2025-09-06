package org.example.finalprojecttuwaiq.DTO;

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
public class RequirementRequestDTO {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Pattern(regexp = "^(Business|Functional|NonFunctional|Regulatory|Constraint)$", message = "Type must be one of Business, Functional, NonFunctional, Regulatory, Constraint")
    private String type;

    @Pattern(regexp = "^(Must|Should|Could|Wont)$", message = "Priority must be one of Must, Should, Could, Wont, High, Medium, Low")
    private String priority;



    @Size(max = 255, message = "Source cannot exceed 255 characters")
    private String source;

    @Size(max = 1000, message = "Rationale cannot exceed 1000 characters")
    private String rationale;


    @NotNull(message = "Project ID cannot be null")
    private Integer projectId;
}
