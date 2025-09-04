package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.Approval;
import org.example.finalprojecttuwaiq.Model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
    List<Approval> getApprovalsByStakeholderAndStatus(Stakeholder stakeholder, String status);
}
