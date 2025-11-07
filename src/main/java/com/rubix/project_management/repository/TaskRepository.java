package com.rubix.project_management.repository;

import com.rubix.project_management.entity.Task;
import com.rubix.project_management.enums.Priority;
import com.rubix.project_management.enums.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByProjectId(Long projectId, Sort sort);
    Optional<Task> findByIdAndProjectId(Long id, Long projectId);

    @Query("SELECT t FROM Task t WHERE t.project.user.id = :userId")
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.project.user.id = :userId " +
            "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Task> searchTasksByUser(@Param("userId") Long userId,
                                 @Param("search") String search,
                                 Sort sort);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority)")
    List<Task> findByProjectIdWithFilters(@Param("projectId") Long projectId,
                                          @Param("status") Status status,
                                          @Param("priority") Priority priority,
                                          Sort sort);
}