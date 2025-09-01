package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
}
