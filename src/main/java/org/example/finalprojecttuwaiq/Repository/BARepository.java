package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.BA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BARepository extends JpaRepository<BA, Integer> {
    BA findBAById(Integer id);
}
