package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, Integer> {
    Stakeholder findStakeholderById(Integer id);

    @Query("select s.projects from Stakeholder s where s.id=?1")
    List<Project> findProjectsByStakeholderId(Integer id);
}
