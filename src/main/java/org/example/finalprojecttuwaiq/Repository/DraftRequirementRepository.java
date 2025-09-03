package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.DraftRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftRequirementRepository extends JpaRepository<DraftRequirement,Integer> {

    DraftRequirement findDraftRequirementById(Integer id);

    @Query("select d from DraftRequirement d where d.project_id=?1")
    List<DraftRequirement> findDraftRequirementByProject_id(Integer project_id);
}
