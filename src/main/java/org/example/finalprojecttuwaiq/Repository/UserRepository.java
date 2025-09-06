package org.example.finalprojecttuwaiq.Repository;

import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserById(Integer id);

    User getUserByUsername(String username);

}
