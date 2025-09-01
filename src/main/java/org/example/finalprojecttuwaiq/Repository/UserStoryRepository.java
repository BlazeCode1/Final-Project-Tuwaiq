package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Integer> {
}
