package org.example.finalprojecttuwaiq.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String asA;

    private String iWant;


    private String soThat;

    @Pattern(regexp = "^(Must|Should|Could|Wont)$", message = "Priority must be one of Must, Should, Could, Wont, High, Medium, Low")
    private String priority;


    private String acceptanceCriteria;

    private String status;

    @NotNull(message = "Requirement cannot be null")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;
}
