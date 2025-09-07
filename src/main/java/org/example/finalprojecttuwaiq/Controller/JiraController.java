package org.example.finalprojecttuwaiq.Controller;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Model.User;
import org.example.finalprojecttuwaiq.Service.JiraService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jira")
public class JiraController {
    private final JiraService jiraService;

    @PostMapping("/issue-from-userstory/{userStoryId}")
    public Map<String, Object> createFromUserStory(@AuthenticationPrincipal User user, @PathVariable Integer userStoryId) {
        return jiraService.createIssueFromUserStoryId(user.getId(),userStoryId, "BAC");
    }

    @PostMapping("/issue-from-project/{projectId}")
    public Map<String, Object> createFromProject(@AuthenticationPrincipal User user,@PathVariable Integer projectId) {
        return jiraService.createIssuesForProject(user.getId(),projectId, "BAC");
    }

    @PostMapping("/project/create/example")
    public Map<String, Object> createNewJiraProject(){
        return jiraService.createNewJiraProject();
    }
}
