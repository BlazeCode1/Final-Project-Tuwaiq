package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer> {
    List<Requirement> getRequirementsByProject(Project project);

    Requirement getRequirementById(Integer id);
}
