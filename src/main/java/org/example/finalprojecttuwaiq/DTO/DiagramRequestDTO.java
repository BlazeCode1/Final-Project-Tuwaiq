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
public class DiagramRequestDTO {
    @Pattern(regexp = "^(UseCase|Class|Sequence|ERD)$", message = "Type must be one of UseCase, Class, Sequence, ERD")
    private String type;

    @NotBlank(message = "Content URI cannot be blank")
    @Size(max = 2048, message = "Content URI cannot exceed 2048 characters")
    private String contentURI;

    @Min(value = 1, message = "Version must be at least 1")
    private int version;

    @NotNull(message = "Project ID cannot be null")
    private Integer projectId;
}
