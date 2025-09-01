package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRepository extends JpaRepository<User, Integer> {
}
