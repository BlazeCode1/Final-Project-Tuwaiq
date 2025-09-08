package org.example.finalprojecttuwaiq;

import org.example.finalprojecttuwaiq.DTO.ProjectRequestDTO;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    BARepository baRepository;

    List<Project> projects;
    User user;
    BA ba;
    Project project1, project2;

    @BeforeEach
    public void setUp() {
        user = new User(1, "Faisal Almansour", "faisal", "faisal@gmail.com", "0557654321", "password", "BA", LocalDateTime.now(), null, null, null);

        ba = new BA();
        ba.setId(1);
        ba.setUser(user);
        ba.setDomainExpertise("IT");
        ba.setIsSubscribed(true);
        ba.setProjects(new HashSet<>());

        project1 = new Project();
        project1.setId(1);
        project1.setName("Project 1");
        project1.setDescription("Description 1");
        project1.setOwner(1);
        project1.setStatus("Discovery");
        project1.setBas(new HashSet<>(Collections.singletonList(ba)));

        project2 = new Project();
        project2.setId(2);
        project2.setName("Project 2");
        project2.setDescription("Description 2");
        project2.setStatus("Discovery");

        projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
    }

    @Test
    public void TestGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> projectList = projectService.getAllProjects();
        Assertions.assertEquals(2, projectList.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void TestGetProjectById() {
        when(baRepository.findBAById(1)).thenReturn(ba);
        when(projectRepository.findProjectById(1)).thenReturn(project1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project1));

        Project foundProject = projectService.getProjectById(1, 1);

        Assertions.assertNotNull(foundProject);
        Assertions.assertEquals("Project 1", foundProject.getName());
        verify(baRepository, times(1)).findBAById(1);
        verify(projectRepository, times(1)).findProjectById(1);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    public void TestAddProject() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
        projectRequestDTO.setName("New Project");
        projectRequestDTO.setDescription("New Description");

        when(baRepository.findBAById(1)).thenReturn(ba);

        projectService.addProject(1, projectRequestDTO);

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(baRepository, times(1)).save(any(BA.class));
    }

    @Test
    public void TestUpdateProject() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
        projectRequestDTO.setName("Updated Project");
        projectRequestDTO.setDescription("Updated Description");

        when(baRepository.findBAById(1)).thenReturn(ba);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project1));
        when(baRepository.findBAById(project1.getOwner())).thenReturn(ba);


        projectService.updateProject(1, 1, projectRequestDTO);

        Assertions.assertEquals("Updated Project", project1.getName());
        Assertions.assertEquals("Updated Description", project1.getDescription());
        Assertions.assertNotNull(project1.getUpdatedAt());
        verify(projectRepository, times(1)).save(project1);
    }

    @Test
    public void TestDeleteProject() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project1));
        when(baRepository.findBAById(1)).thenReturn(ba);
        when(baRepository.findBAById(project1.getOwner())).thenReturn(ba);

        projectService.deleteProject(1, 1);

        verify(projectRepository, times(1)).delete(project1);
    }
}
