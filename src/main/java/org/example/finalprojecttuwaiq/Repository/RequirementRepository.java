package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer> {
}
