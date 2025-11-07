package com.rubix.project_management.controller;


import com.rubix.project_management.dto.ProjectRequest;
import com.rubix.project_management.dto.ProjectResponse;
import com.rubix.project_management.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            Authentication authentication,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.createProject(authentication.getName(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(Authentication authentication) {
        List<ProjectResponse> projects = projectService.getAllProjects(authentication.getName());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            Authentication authentication,
            @PathVariable Long id) {
        ProjectResponse project = projectService.getProjectById(authentication.getName(), id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.updateProject(authentication.getName(), id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            Authentication authentication,
            @PathVariable Long id) {
        projectService.deleteProject(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
