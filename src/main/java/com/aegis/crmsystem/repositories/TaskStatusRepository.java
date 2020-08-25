package com.aegis.crmsystem.repositories;

import com.aegis.crmsystem.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
