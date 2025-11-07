package com.rubix.project_management.service;

import com.rubix.project_management.dto.ProjectRequest;
import com.rubix.project_management.dto.ProjectResponse;
import com.rubix.project_management.entity.Project;
import com.rubix.project_management.entity.User;
import com.rubix.project_management.exception.ResourceNotFoundException;
import com.rubix.project_management.repository.ProjectRepository;
import com.rubix.project_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectResponse createProject(String username, ProjectRequest request) {
        User user = getUserByUsername(username);

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUser(user);

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects(String username) {
        User user = getUserByUsername(username);
        return projectRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(String username, Long projectId) {
        User user = getUserByUsername(username);
        Project project = projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return mapToResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(String username, Long projectId, ProjectRequest request) {
        User user = getUserByUsername(username);
        Project project = projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject);
    }

    @Transactional
    public void deleteProject(String username, Long projectId) {
        User user = getUserByUsername(username);
        Project project = projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        projectRepository.delete(project);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}