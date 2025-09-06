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

    @GetMapping("/whoami")
    public Map<String,Object> who() { return jiraService.whoAmI(); }

    @GetMapping("/types")
    public Map<String,Object> types(@RequestParam String projectKey) {
        return jiraService.getCreateMeta(projectKey);
    }

    // إنشاء Task (GET للتجربة)
    @GetMapping("/createTask")
    public Map<String,Object> createTaskGet(@RequestParam String projectKey,
                                            @RequestParam String summary,
                                            @RequestParam String desc) {
        return jiraService.createTask(projectKey, summary, desc);
    }

    // إنشاء Task (POST المعياري)
    @PostMapping("/createTask")
    public Map<String,Object> createTaskPost(@RequestParam String projectKey,
                                             @RequestParam String summary,
                                             @RequestParam String desc) {
        return jiraService.createTask(projectKey, summary, desc);
    }

    // إرفاق ملف (مرّر مسار الملف على السيرفر)
    @GetMapping("/attach")
    public Object attach(@RequestParam String key, @RequestParam String path) {
        return jiraService.attach(key, new java.io.File(path));
    }

    @PostMapping("/comment")
    public Object comment(@RequestParam String key, @RequestParam String text) {
        return jiraService.addComment(key, text);
    }

    @PostMapping("/assignMe")
    public void assignMe(@RequestParam String key) {
        jiraService.assignToMe(key);
    }

    @PostMapping("/priority")
    public void priority(@RequestParam String key, @RequestParam String name) {
        jiraService.setPriority(key, name); // مثال: High
    }

    @PostMapping("/start")
    public void start(@RequestParam String key) {
        jiraService.transitionTo(key, "In Progress"); // غيّر الاسم لو وركفلو مختلف
    }

    @PostMapping("/create-from-userstory/{userStoryId}")
    public Map<String, Object> createFromUserStory(@PathVariable Integer userStoryId) {
        return jiraService.createIssueFromUserStoryId(userStoryId, "BAC");
    }

}
