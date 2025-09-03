package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.DraftUserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftUserStoryRepository extends JpaRepository<DraftUserStory,Integer> {

    DraftUserStory findDraftUserStoryById(Integer id);

    @Query("select d from DraftUserStory d where d.requirement_id=?1")
    List<DraftUserStory> findDraftUserStoriesByRequirement_id(Integer requirement_id);
}
