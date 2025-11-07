package com.rubix.project_management.dto;

import com.rubix.project_management.enums.Priority;
import com.rubix.project_management.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

