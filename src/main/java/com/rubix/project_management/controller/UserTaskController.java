package com.rubix.project_management.controller;


import com.rubix.project_management.dto.TaskResponse;
import com.rubix.project_management.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class UserTaskController {

    private final TaskService taskService;

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchAllTasks(
            Authentication authentication,
            @RequestParam String query,
            @RequestParam(required = false) String sortBy) {
        List<TaskResponse> tasks = taskService.searchTasks(
                authentication.getName(), query, sortBy);
        return ResponseEntity.ok(tasks);
    }
}

