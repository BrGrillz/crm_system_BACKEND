package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.CreateTaskDto;
import com.aegis.crmsystem.dto.request.task.DetailsTaskDto;
import com.aegis.crmsystem.dto.request.task.FilterGetAllTaskDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionAccessDenied;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.models.Comment;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.TaskStatus;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.CommentRepository;
import com.aegis.crmsystem.repositories.TaskRepository;
import com.aegis.crmsystem.repositories.TaskStatusRepository;
import com.aegis.crmsystem.repositories.UserRepository;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public Task create(
            @NotNull CreateTaskDto createTaskDto,
            @NotNull User user
    ) {
        User responsibleUser = userRepository.findById(createTaskDto.getResponsible()).get();

        List<User> observersUsers = null;
        if(createTaskDto.getObservers() != null){
            observersUsers = userRepository.findAllById(createTaskDto.getObservers());
        }

        log.debug("-------------------------------observersUsers--------------------------------------- {}", observersUsers);

        TaskStatus taskStatus = taskStatusRepository.findById(1L).get();

        return tasksRepository.save(Task.builder()
                .title(createTaskDto.getTitle())
                .description(createTaskDto.getDescription())
                .dueDate(createTaskDto.getDueDate())
                .deleteStatus(false)
                .status(taskStatus)
                .author(user)
                .responsible(responsibleUser)
                .observers(observersUsers)
                .build());
    }

    public Task getById(
            @NotNull DetailsTaskDto detailsTaskDto,
            @NotNull User user
            ) {
        log.info(SwaggerConst.Tasks.GET_TASK_BY_ID);

        final Task task = tasksRepository.findById(detailsTaskDto.getId()).get();
        if(task == null) throw new ApiRequestExceptionNotFound("Задача по такому id не найден");

        final Long userId = user.getId();
        final Long authorId = task.getAuthor().getId();
        final Long responsibleId = task.getResponsible().getId();

        if(!userId.equals(authorId) && !userId.equals(responsibleId)){
            throw new ApiRequestExceptionAccessDenied("У вас нету доступа для просмотра этой задачи");
        }

        return task;
    }

    public List<Task> findAll(User user, FilterGetAllTaskDto filterGetAllTaskDto) {
        log.info(SwaggerConst.Tasks.GET_ALL_TASKS);

        if(filterGetAllTaskDto.getAuthor() != null){
            return tasksRepository.findWhereUserAuthor(user);
        }

        if(filterGetAllTaskDto.getResponsible() != null){
            return tasksRepository.findWhereUserResponsible(user);
        }

        if(filterGetAllTaskDto.getObservers() != null){
            return tasksRepository.findWhereUserObserver(user);
        }

        return tasksRepository.findAllByUser(user);
    }

    public List<TaskStatus> findAllStatus() {
        return taskStatusRepository.findAll();
    }

    public Task put(
            @NotNull Task task,
            @NotNull String title,
            @NotNull String text,
            @NotNull Long responsible,
            List<Long> observers,
            @NotNull Date dueDate,
            @NotNull User user
            ) {
        log.info(SwaggerConst.Tasks.UPDATE_TASK);

        final Long userId = user.getId();
        final Long authorId = task.getAuthor().getId();
        final Long responsibleId = task.getResponsible().getId();

        if(!userId.equals(authorId) && !userId.equals(responsibleId)){
            throw new ApiRequestExceptionAccessDenied("У вас нет доступа для выполнения этой операции");
        }

        User userResponsible = userRepository.findById(responsible).get();

        task.setTitle(title);
        task.setDescription(text);
        task.setResponsible(userResponsible);
        task.setDueDate(dueDate);

        return tasksRepository.save(task);
    }

    public Task patch(
            @NotNull Task task,
            String title,
            String text,
            Long responsible,
            List<Long> observers,
            Date dueDate,
            @NotNull User user
    ) {
        log.info(SwaggerConst.Tasks.UPDATE_TASK);

        final Long userId = user.getId();
        final Long authorId = task.getAuthor().getId();
        final Long responsibleId = task.getResponsible().getId();

        if(!userId.equals(authorId) || !userId.equals(responsibleId)){
            throw new ApiRequestExceptionAccessDenied("У вас нет доступа для выполнения этой операции");
        }

        if(title != null){
            task.setTitle(title);
        }

        if(text != null){
            task.setDescription(text);
        }

        if(responsible != null){
            User userResponsible = userRepository.findById(responsible).get();
            task.setResponsible(userResponsible);
        }

//        if(!observers != null){
//            task.setTitle(title);
//        }

        if(dueDate != null){
            task.setDueDate(dueDate);
        }

        return tasksRepository.save(task);
    }

    public void delete(@NotNull Task task) {
        log.info(SwaggerConst.Tasks.DELETE_TASK);

        tasksRepository.delete(task);
    }

    public Comment makeComment(
            @NotNull CommentDto commentDto,
            @NotNull User user
    ) {
        Task task = tasksRepository.findById(Long.valueOf(commentDto.getTaskId())).get();

        return commentRepository.save(Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .task(task)
                .build());
    }
}
