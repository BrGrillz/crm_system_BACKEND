package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.TaskRepository;
import com.aegis.crmsystem.repositories.UserRepository;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    public Task create(
            @NotNull String title,
            @NotNull String text
    ) {
        log.info(SwaggerConst.Tasks.CREATE_TASK + title + text);

        User user = userRepository.findById(new Long(1)).get();

        return tasksRepository.save(Task.builder()
                .title(title)
                .text(text)
                .author(user)
                .responsible(user)
                .observer(user)
//                .creat_date(LocalDateTime.now())
//                .update_date(LocalDateTime.now())
                .build());
    }

    public Task getById(@NotNull Task task) {
        log.info(SwaggerConst.Tasks.GET_TASK_BY_ID);

        if(task == null) throw new ApiRequestExceptionNotFound("Пользователь по такому id не найден");

        return task;
    }

    public List<Task> findAll() {
        log.info("${SwaggerConst.Tasks.GET_ALL_TASKS} {}", SwaggerConst.Tasks.GET_ALL_TASKS);

        return tasksRepository.findAll();
    }

    public Task update(
            @NotNull Task taskFromDb,
            @NotNull String title,
            @NotNull String text
    ) {
        log.info(SwaggerConst.Tasks.UPDATE_TASK);

        return tasksRepository.save(Task.builder()
                .id(taskFromDb.getId())
                .title(title)
                .text(text)
//                .creat_date(taskFromDb.getCreat_date())
//                .update_date(LocalDateTime.now())
                .build());
    }

    public void delete(@NotNull Task task) {
        log.info(SwaggerConst.Tasks.DELETE_TASK);

        tasksRepository.delete(task);
    }
}
