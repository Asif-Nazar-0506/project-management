package com.rubix.project_management.controller;

import com.rubix.project_management.dto.TaskRequest;
import com.rubix.project_management.dto.TaskResponse;
import com.rubix.project_management.enums.Priority;
import com.rubix.project_management.enums.Status;
import com.rubix.project_management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            Authentication authentication,
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.createTask(authentication.getName(), projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            Authentication authentication,
            @PathVariable Long projectId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String sortBy) {
        List<TaskResponse> tasks = taskService.getAllTasks(
                authentication.getName(), projectId, status, priority, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            Authentication authentication,
            @RequestParam String query,
            @RequestParam(required = false) String sortBy) {
        List<TaskResponse> tasks = taskService.searchTasks(
                authentication.getName(), query, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            Authentication authentication,
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        TaskResponse task = taskService.getTaskById(authentication.getName(), projectId, taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            Authentication authentication,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.updateTask(
                authentication.getName(), projectId, taskId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            Authentication authentication,
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        taskService.deleteTask(authentication.getName(), projectId, taskId);
        return ResponseEntity.noContent().build();
    }
}