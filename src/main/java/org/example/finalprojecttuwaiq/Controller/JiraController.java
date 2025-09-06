package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Service.JiraService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jira")
public class JiraController {
    private final JiraService jiraService;

    @PostMapping("/issue-from-userstory/{userStoryId}")
    public Map<String, Object> createFromUserStory(@PathVariable Integer userStoryId) {
        return jiraService.createIssueFromUserStoryId(userStoryId, "BAC");
    }

    @PostMapping("/issue-from-project/{projectId}")
    public Map<String, Object> createFromProject(@PathVariable Integer projectId) {
        return jiraService.createIssuesForProject(projectId, "BAC");
    }

    @PostMapping("/project/create/example")
    public Map<String, Object> createNewJiraProject(){
        return jiraService.createNewJiraProject();
    }
}
