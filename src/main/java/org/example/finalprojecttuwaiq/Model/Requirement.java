package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(unique = true)
    private String title;

    @NotEmpty(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Pattern(regexp = "^(Business|Functional|NonFunctional|Regulatory|Constraint)$", message = "Type must be one of Business, Functional, NonFunctional, Regulatory, Constraint")
    private String type;

    @Pattern(regexp = "^(Must|Should|Could|Wont|High|Medium|Low)$", message = "Priority must be one of Must, Should, Could, Wont, High, Medium, Low")
    private String priority;

    @Pattern(regexp = "^(Draft|Analyzed|Validated|Approved|Deprecated)$", message = "Status must be one of Draft, Analyzed, Validated, Approved, Deprecated")
    private String status;

    @Size(max = 255, message = "Source cannot exceed 255 characters")
    private String source;

    @Size(max = 1000, message = "Rationale cannot exceed 1000 characters")
    private String rationale;


    @ManyToOne
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private Set<UserStory> userStories;
}
