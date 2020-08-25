package com.aegis.crmsystem.repositories;

import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("FROM Task t WHERE t.author = :user OR t.responsible = :user")
    List<Task> findAllByUser(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.author = :user")
    List<Task> findWhereUserAuthor(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.responsible = :user")
    List<Task> findWhereUserResponsible(
            @Param("user") User user
    );

    @Query("FROM Task t WHERE t.observers IN :user ")
    List<Task> findWhereUserObserver(
            @Param("user") User user
    );
}
