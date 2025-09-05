package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BARepository extends JpaRepository<BA, Integer> {
    BA findBAById(Integer id);

    @Query("select b.projects from BA b where b.id=?1")
    List<Project> findProjectsByBaID(Integer id);

}
