package org.example.finalprojecttuwaiq.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^(BRD|FRD)$", message = "Type must be one of BRD, FRD, UserStoryPack, ValidationReport, DesignDoc")
    private String type;

    @NotEmpty(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

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
    private Project project;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private Set<Approval> approvals;
}
