package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Domain expertise cannot be blank")
    @Size(max = 255, message = "Domain expertise cannot exceed 255 characters")
    private String domainExpertise;

    @Column(columnDefinition = "boolean default false")
    // can be null
    Boolean isSubscribed = false;

    @Column(columnDefinition = "date")
    // can be null
    LocalDate subscriptionStartDate;

    @Column(columnDefinition = "date")
    // can be null
    LocalDate subscriptionEndDate;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "ba", cascade = CascadeType.ALL)
    private Set<Approval> approvals;

    @ManyToMany
    @JoinTable(
        name = "ba_project",
        joinColumns = @JoinColumn(name = "ba_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();
}
