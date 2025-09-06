package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Service.JiraService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jira")
public class JiraController {
    private final JiraService jiraService;

    @PostMapping("/create-from-userstory/{userStoryId}")
    public Map<String, Object> createFromUserStory(@PathVariable Integer userStoryId) {
        return jiraService.createIssueFromUserStoryId(userStoryId, "BAC");
    }

    @PostMapping("/create-from-project/{projectId}")
    public Map<String, Object> createFromProject(@PathVariable Integer projectId) {
        return jiraService.createIssuesForProject(projectId, "BAC");
    }

    @PostMapping("/project/create/example")
    public Map<String, Object> createNewJiraProject(){
        return jiraService.createNewJiraProject();
    }
}
