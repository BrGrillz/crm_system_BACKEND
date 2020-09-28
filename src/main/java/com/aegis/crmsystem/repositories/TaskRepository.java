package com.aegis.crmsystem.repositories;

import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {

    @Query("FROM Task t WHERE (t.author = :user OR t.responsible = :user OR :user MEMBER OF t.observers) AND t.deleteStatus = false ORDER BY t.id DESC")
    List<Task> findAllByUser(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.author = :user AND t.deleteStatus = false ORDER BY t.id DESC")
    List<Task> findWhereUserAuthor(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.responsible = :user AND t.deleteStatus = false ORDER BY t.id DESC")
    List<Task> findWhereUserResponsible(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE :user MEMBER OF t.observers AND t.deleteStatus = false ORDER BY t.id DESC")
    List<Task> findWhereUserObserver(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.deleteStatus = true ORDER BY t.id DESC")
    List<Task> findDeleted();

}

//@Query("from Form f LEFT JOIN f.instances JOIN f.groups g WHERE f.groups IN (?1) group by f.id")