package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;

import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Model.Requirement;
import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.example.finalprojecttuwaiq.Repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JiraService {

    private final RestTemplate rest;
    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;

    @Value("${jira.base.url}")
    private String base;
    @Value("${jira.email}")
    private String email;
    @Value("${jira.api.token}")
    private String token;

    // for testing, TODO replace later
    @Value("${jira.lead.account.id}")
    private String leadAccountId;

    private HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setBasicAuth(email, token);               // email + API token
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    public Map<String, Object> createIssueFromUserStoryId(Integer userStoryId, String jiraProjectKey) {
        UserStory us = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory " + userStoryId + " not found"));

        String asA = us.getAsA() == null ? "" : us.getAsA().trim();
        String iWant = us.getIWant() == null ? "" : us.getIWant().trim();
        String soThat = us.getSoThat() == null ? "" : us.getSoThat().trim();

        String summary = ("As a " + asA + ", I want " + iWant +
                (soThat.isBlank() ? "" : " so that " + soThat)).trim();
        if (summary.length() > 240) summary = summary.substring(0, 240);

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("project", Map.of("key", jiraProjectKey));
        fields.put("issuetype", Map.of("id", "10007")); // Task
        fields.put("summary", summary);

        var req = new HttpEntity<>(Map.of("fields", fields), jsonHeaders());
        var res = rest.exchange(
                base + "/rest/api/3/issue",
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        ).getBody();

        String jiraKey = (String) res.get("key");
        String self = (String) res.get("self");

        us.getRequirement().getProject().setStatus("InDelivery");

        return Map.of(
                "status", "created",
                "userStoryId", userStoryId,
                "jiraKey", jiraKey,
                "browse", base + "/browse/" + jiraKey,
                "self", self
        );
    }

    public Map<String, Object> createIssuesForProject(Integer projectId, String jiraProjectKey) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new RuntimeException("Project " + projectId + " not found");

        // collect all US from all requirements
        List<UserStory> stories = new ArrayList<>();
        if (project.getRequirements() != null) {
            for (Requirement r : project.getRequirements()) {
                if (r.getUserStories() != null) stories.addAll(r.getUserStories());
            }
        }

        List<Map<String, Object>> items = new ArrayList<>();
        for (UserStory us : stories) {
            // reuse the simple single-US creation method
            Map<String, Object> result = createIssueFromUserStoryId(us.getId(), jiraProjectKey);
            items.add(result);
        }

        long created = items.stream()
                .filter(m -> "created".equals(m.get("status")))
                .count();

        project.setStatus("InDelivery");

        return Map.of(
                "projectId", projectId,
                "projectKey", jiraProjectKey,
                "totalUserStories", stories.size(),
                "createdCount", created,
                "items", items
        );
    }

    // sample for now
    public Map<String, Object> createNewJiraProject(){

        Map<String, Object> payload = new LinkedHashMap<>();
        // the following data are a sample from Atlassian themselves, not the map however
        payload.put("key", "EX08"); // Ensure this key is unique and not already in use
        payload.put("name", "Example");
        payload.put("projectTypeKey", "business"); // "software", "service_desk", "business"
        payload.put("projectTemplateKey", "com.atlassian.jira-core-project-templates:jira-core-simplified-process-control"); // Example for business, use appropriate for your type
        payload.put("description", "Cloud migration initiative");
        payload.put("leadAccountId", leadAccountId);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, jsonHeaders());
        var res = rest.exchange(
                base + "/rest/api/3/project",
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        ).getBody();

        String jiraKey = (String) res.get("key");
        String self = (String) res.get("self");
        Integer id = (Integer) res.get("id"); // TODO check this one for correct return type

        return Map.of(
                "status", "created",
                "projectId", id,
                "jiraKey", jiraKey,
                "browse", base + "/browse/" + jiraKey,
                "self", self
        );
    }

}

