package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Column(columnDefinition = "longtext")
    private String description;

    //TODO: After Generating Requirements Project Status Changes To Analysis
    //TODO: Project Status Chagnes to Documentation After Generating Any Document
    //TODO: When Requesting Appro vals to stakeholders, Project Status Changes To Validation
    //TODO: When Requesting Tools for tech solutions, Project Status Changes To InDelivery
    @Pattern(regexp = "^(Discovery|Analysis|Documentation|Validation|InDelivery)$", message = "Status must be one of Discovery, Analysis, Documentation, Validation, Design, InDelivery")
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "projects")
    private Set<BA> bas = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    private Set<Stakeholder> stakeholders;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Requirement> requirements;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Document> documents;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Diagram> diagrams;
}
