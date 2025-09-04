package org.example.finalprojecttuwaiq.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED)$", message = "Status must be one of PENDING, APPROVED, REJECTED")
    private String status;



    @Lob
    @Size(max = 2000, message = "Comments cannot exceed 2000 characters")
    private String comments;

    @NotNull(message = "Reviewed at date cannot be null")
    private LocalDateTime reviewedAt;

    @NotNull(message = "Business analyst cannot be null")
    @ManyToOne
    @JoinColumn(name = "ba_id")
    private BA ba;

    @NotNull(message = "Stakeholder cannot be null")
    @ManyToOne
    @JoinColumn(name = "stakeholder_id")
    private Stakeholder stakeholder;

    @NotNull(message = "Document cannot be null")
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
}
