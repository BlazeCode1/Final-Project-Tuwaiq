package org.example.finalprojecttuwaiq;

import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BARepository baRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    BA ba;
    Project project1, project2;

    List<Project> projects;

    @BeforeEach
    public void setUp() {
        // create and save user
        user = new User(null, "Faisal Almansour", "memememe", "memememe@gmail.com",
                "0557654321", "password", "BA", LocalDateTime.now(), null, null, null);
        userRepository.save(user);

        // create and save BA
        ba = new BA();
        ba.setUser(user);
        ba.setDomainExpertise("IT");
        ba.setIsSubscribed(true);
        baRepository.save(ba);

        // create projects
        project1 = new Project(null, "Project 1", "Description 1", "Discovery",
                null, null, ba.getId(), null, null, null, null, null);
        project1.setBas(new HashSet<>());
        project1.getBas().add(ba);

        project2 = new Project(null, "Project 2", "Description 2", "Discovery",
                null, null, ba.getId(), null, null, null, null, null);
        project2.setBas(new HashSet<>());
        project2.getBas().add(ba);
    }

    @Test
    public void TestFindProjectById() {
        projectRepository.save(project1);
        Project project = projectRepository.findProjectById(project1.getId());
        Assertions.assertThat(project).isEqualTo(project1);
    }

    @Test
    public void TestFindAllProjects() {
        projectRepository.save(project1);
        projectRepository.save(project2);
        projects = projectRepository.findAll();
        Assertions.assertThat(projects.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void TestSaveProject() {
        Project savedProject = projectRepository.save(project1);
        Assertions.assertThat(savedProject).isNotNull();
        Assertions.assertThat(savedProject.getId()).isNotNull();
    }

    @Test
    public void TestDeleteProject() {
        projectRepository.save(project1);
        projectRepository.delete(project1);
        Project deleted = projectRepository.findProjectById(project1.getId());
        Assertions.assertThat(deleted).isNull();
    }

    @Test
    public void TestUpdateProject() {
        projectRepository.save(project1);
        Project project = projectRepository.findProjectById(project1.getId());
        project.setName("Updated Project");
        Project updatedProject = projectRepository.save(project);
        Assertions.assertThat(updatedProject.getName()).isEqualTo("Updated Project");
    }


}
