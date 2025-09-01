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
public class DocumentRequestDTO {
    @Pattern(regexp = "^(BRD|FRD|UserStoryPack|ValidationReport|DesignDoc)$", message = "Type must be one of BRD, FRD, UserStoryPack, ValidationReport, DesignDoc")
    private String type;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Content URI cannot be blank")
    @Size(max = 2048, message = "Content URI cannot exceed 2048 characters")
    private String contentURI;

    @Min(value = 1, message = "Version must be at least 1")
    private int version;

    @NotNull(message = "Project ID cannot be null")
    private Integer projectId;
}
