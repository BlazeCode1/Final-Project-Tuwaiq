package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Stakeholder {
    @Id
    private Integer id;

    @NotEmpty(message = "Organization cannot be blank")
    @Size(max = 100, message = "Organization cannot exceed 100 characters")
    private String organization;

    @NotEmpty(message = "Position cannot be Empty")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    @Pattern(regexp = "^(Sponsor|Reviewer)$")
    private String role_in_project;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(
        name = "stakeholder_project",
        joinColumns = @JoinColumn(name = "stakeholder_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;

    @OneToMany(mappedBy = "stakeholder", cascade = CascadeType.ALL)
    private Set<Approval> approvals;
}
