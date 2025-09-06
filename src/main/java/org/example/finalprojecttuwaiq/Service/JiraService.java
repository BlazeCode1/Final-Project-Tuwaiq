package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;

import org.example.finalprojecttuwaiq.Model.UserStory;
import org.example.finalprojecttuwaiq.Repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JiraService {

    private final RestTemplate rest;
    private final UserStoryRepository userStoryRepository;

    @Value("${jira.base-url}")
    private String base;
    @Value("${jira.email}")
    private String email;
    @Value("${jira.api-token}")
    private String token;

    private HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setBasicAuth(email, token);               // email + API token
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    public Map<String,Object> whoAmI() {
        var req = new HttpEntity<>(jsonHeaders());
        var url = base + "/rest/api/3/myself";
        return rest.exchange(url, HttpMethod.GET, req,
                new ParameterizedTypeReference<Map<String,Object>>(){}).getBody();
    }

    public Map<String,Object> getCreateMeta(String projectKey) {
        var req = new HttpEntity<>(jsonHeaders());
        var url = base + "/rest/api/3/issue/createmeta?projectKeys=" + projectKey + "&expand=projects.issuetypes.fields";
        return rest.exchange(url, HttpMethod.GET, req,
                new ParameterizedTypeReference<Map<String,Object>>(){}).getBody();
    }

    public Map<String,Object> createTask(String projectKey, String summary, String desc) {
        Map<String,Object> adf = Map.of(
                "type","doc","version",1,
                "content", List.of(Map.of(
                        "type","paragraph",
                        "content", List.of(Map.of("type","text","text", desc))
                ))
        );

        Map<String,Object> payload = Map.of(
                "fields", Map.of(
                        "project",   Map.of("key", projectKey),
                        "issuetype", Map.of("id", "10007"), // Task
                        "summary",   summary,
                        "description", adf
                )
        );

        var req = new HttpEntity<>(payload, jsonHeaders());
        var url = base + "/rest/api/3/issue";
        return rest.exchange(url, HttpMethod.POST, req,
                new ParameterizedTypeReference<Map<String,Object>>(){}).getBody();
    }

    // إرفاق ملف PDF (مثلاً BRD)
    public Map<String,Object>[] attach(String issueKey, File file) {
        HttpHeaders h = new HttpHeaders();
        h.setBasicAuth(email, token);
        h.add("X-Atlassian-Token", "no-check");
        h.setContentType(MediaType.MULTIPART_FORM_DATA);

        var body = new org.springframework.util.LinkedMultiValueMap<String, Object>();
        body.add("file", new org.springframework.core.io.FileSystemResource(file));

        var req = new HttpEntity<>(body, h);
        var url = base + "/rest/api/3/issue/{key}/attachments";
        return rest.postForObject(url, req, Map[].class, issueKey);
    }

    // تعليق نصّي بسيط
    public Map<String,Object> addComment(String issueKey, String text) {
        var adf = Map.of(
                "type","doc","version",1,
                "content", List.of(Map.of("type","paragraph",
                        "content", List.of(Map.of("type","text","text", text))))
        );
        var req = new HttpEntity<>(Map.of("body", adf), jsonHeaders());
        var url = base + "/rest/api/3/issue/" + issueKey + "/comment";
        return rest.exchange(url, HttpMethod.POST, req,
                new ParameterizedTypeReference<Map<String,Object>>(){}).getBody();
    }

    // عيّن التذكرة لنفسك
    public void assignToMe(String issueKey) {
        var me = whoAmI(); // عندك جاهزة
        var req = new HttpEntity<>(Map.of("accountId", me.get("accountId")), jsonHeaders());
        var url = base + "/rest/api/3/issue/" + issueKey + "/assignee";
        rest.exchange(url, HttpMethod.PUT, req, Void.class);
    }

    // غيّر الأولوية بالاسم (High, Medium, Low...)
    public void setPriority(String issueKey, String priorityName) {
        var fields = Map.of("fields", Map.of("priority", Map.of("name", priorityName)));
        var req = new HttpEntity<>(fields, jsonHeaders());
        var url = base + "/rest/api/3/issue/" + issueKey;
        rest.exchange(url, HttpMethod.PUT, req, Void.class);
    }

    // حوّل الحالة بالاسم (مثلاً "In Progress")
    public void transitionTo(String issueKey, String targetName) {
        // 1) جيب التحويلات المتاحة
        var req = new HttpEntity<>(jsonHeaders());
        var url = base + "/rest/api/3/issue/" + issueKey + "/transitions";
        var res = rest.exchange(url, HttpMethod.GET, req,
                new ParameterizedTypeReference<Map<String,Object>>(){}).getBody();

        var transitions = (List<Map<String,Object>>) res.get("transitions");
        var match = transitions.stream()
                .filter(t -> targetName.equalsIgnoreCase((String)((Map)t.get("to")).get("name"))
                        || targetName.equalsIgnoreCase((String)t.get("name")))
                .findFirst().orElseThrow(() -> new RuntimeException("No transition named: " + targetName));

        var body = Map.of("transition", Map.of("id", match.get("id")));
        var doReq = new HttpEntity<>(body, jsonHeaders());
        rest.exchange(url, HttpMethod.POST, doReq, Void.class);
    }

    public Map<String, Object> createIssueFromUserStoryId(Integer userStoryId, String jiraProjectKey) {
        UserStory us = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory " + userStoryId + " not found"));

        String asA    = us.getAsA()    == null ? "" : us.getAsA().trim();
        String iWant  = us.getIWant()  == null ? "" : us.getIWant().trim();   // انتبه لحرف W الكبير
        String soThat = us.getSoThat() == null ? "" : us.getSoThat().trim();

        String summary = ("As a " + asA + ", I want " + iWant +
                (soThat.isBlank() ? "" : " so that " + soThat)).trim();
        if (summary.length() > 240) summary = summary.substring(0, 240);

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("project",   Map.of("key", jiraProjectKey));
        fields.put("issuetype", Map.of("id", "10007")); // Task
        fields.put("summary",   summary);

        var req = new HttpEntity<>(Map.of("fields", fields), jsonHeaders());
        var res = rest.exchange(
                base + "/rest/api/3/issue",
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        ).getBody();

        String jiraKey = (String) res.get("key");
        String self    = (String) res.get("self");

        return Map.of(
                "status", "created",
                "userStoryId", userStoryId,
                "jiraKey", jiraKey,
                "browse", base + "/browse/" + jiraKey,
                "self", self
        );
    }




}

