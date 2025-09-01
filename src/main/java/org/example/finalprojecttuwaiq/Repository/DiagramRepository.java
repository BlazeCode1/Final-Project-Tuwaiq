package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Diagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagramRepository extends JpaRepository<Diagram, Integer> {
}
