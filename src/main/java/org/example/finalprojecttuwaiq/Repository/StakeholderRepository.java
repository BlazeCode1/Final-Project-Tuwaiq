package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, Integer> {
}
