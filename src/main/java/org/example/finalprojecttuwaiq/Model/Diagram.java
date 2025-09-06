package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diagram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^(UseCase|Class|Sequence|ERD)$", message = "Type must be one of UseCase, Class, Sequence, ERD")
    private String type;

    @NotEmpty(message = "Content URI cannot be blank")
    @Size(max = 2048, message = "Content URI cannot exceed 2048 characters")
    private String contentURI;

    @Min(value = 1, message = "Version must be at least 1")
    private int version;

    @NotNull(message = "Last updated date cannot be null")
    private LocalDateTime lastUpdated;

    @NotNull(message = "Project cannot be null")
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
}
