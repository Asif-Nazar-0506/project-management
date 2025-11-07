package com.rubix.project_management.service;

import com.rubix.project_management.dto.TaskRequest;
import com.rubix.project_management.dto.TaskResponse;
import com.rubix.project_management.entity.Project;
import com.rubix.project_management.entity.Task;
import com.rubix.project_management.entity.User;
import com.rubix.project_management.enums.Priority;
import com.rubix.project_management.enums.Status;
import com.rubix.project_management.exception.ResourceNotFoundException;
import com.rubix.project_management.repository.ProjectRepository;
import com.rubix.project_management.repository.TaskRepository;
import com.rubix.project_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(String username, Long projectId, TaskRequest request) {
        User user = getUserByUsername(username);
        Project project = projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(String username, Long projectId,
                                          Status status, Priority priority,
                                          String sortBy) {
        User user = getUserByUsername(username);
        projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }

        List<Task> tasks;
        if (status != null || priority != null) {
            tasks = taskRepository.findByProjectIdWithFilters(projectId, status, priority, sort);
        } else {
            tasks = taskRepository.findByProjectId(projectId, sort);
        }

        return tasks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> searchTasks(String username, String query, String sortBy) {
        User user = getUserByUsername(username);

        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }

        List<Task> tasks = taskRepository.searchTasksByUser(user.getId(), query, sort);

        return tasks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(String username, Long projectId, Long taskId) {
        User user = getUserByUsername(username);
        projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(String username, Long projectId, Long taskId, TaskRequest request) {
        User user = getUserByUsername(username);
        projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(String username, Long projectId, Long taskId) {
        User user = getUserByUsername(username);
        projectRepository.findByIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}