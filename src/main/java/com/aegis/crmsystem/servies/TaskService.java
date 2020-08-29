package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.*;
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

import java.util.ArrayList;
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
                .status(taskStatus)
                .author(user)
                .deleteStatus(false)
                .responsible(responsibleUser)
                .observers(observersUsers)
                .build());
    }

    public Task getOne(
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

        List<User> observers = new ArrayList<>();
        observers.add(user);

        if(filterGetAllTaskDto.getAuthor() != null){
            return tasksRepository.findWhereUserAuthor(user);
        }

        if(filterGetAllTaskDto.getResponsible() != null){
            return tasksRepository.findWhereUserResponsible(user);
        }

        if(filterGetAllTaskDto.getObservers() != null){
            log.debug("==================observers======================= {}", observers);
            return tasksRepository.findWhereUserObserver(user);
        }

        if(filterGetAllTaskDto.getDeleted() != null){
            return tasksRepository.findDeleted();
        }

        return tasksRepository.findAllByUser(user);
    }

    public List<TaskStatus> findAllStatus() {
        return taskStatusRepository.findAll();
    }

    public Task put(
            @NotNull UpdateTaskDto updateTaskDto,
            @NotNull User user
            ) {
        log.info(SwaggerConst.Tasks.UPDATE_TASK);

        final Task task = tasksRepository.findById(updateTaskDto.getId()).get();

        final Long userId = user.getId();
        final Long authorId = task.getAuthor().getId();
        final Long responsibleId = task.getResponsible().getId();

        if(!userId.equals(authorId) && !userId.equals(responsibleId)){
            throw new ApiRequestExceptionAccessDenied("У вас нет доступа для выполнения этой операции");
        }

        final List<User> userObservers = userRepository.findAllById(updateTaskDto.getObservers());
        final User userResponsible = userRepository.findById(updateTaskDto.getResponsible()).get();
        final TaskStatus status = taskStatusRepository.findById(updateTaskDto.getStatus()).get();


        log.debug("===========updateTaskDto.getDueDate()================== {}", updateTaskDto.getDueDate());

        task.setTitle(updateTaskDto.getTitle());
        task.setDescription(updateTaskDto.getDescription());
        task.setResponsible(userResponsible);
        task.setObservers(userObservers);
        task.setDueDate(updateTaskDto.getDueDate());
        task.setStatus(status);

        return tasksRepository.save(task);
    }

    public Task patch(
            @NotNull PatchTaskDto patchTaskDto,
            @NotNull User user
    ) {
        log.info(SwaggerConst.Tasks.UPDATE_TASK);

        final Task task = tasksRepository.findById(patchTaskDto.getId()).get();

        final Long userId = user.getId();
        final Long authorId = task.getAuthor().getId();
        final Long responsibleId = task.getResponsible().getId();

        if(!userId.equals(authorId) && !userId.equals(responsibleId)){
            throw new ApiRequestExceptionAccessDenied("У вас нет доступа для выполнения этой операции");
        }

        if(patchTaskDto.getTitle() != null){
            task.setTitle(patchTaskDto.getTitle());
        }

        if(patchTaskDto.getDescription() != null){
            task.setDescription(patchTaskDto.getDescription());
        }

        if(patchTaskDto.getResponsible() != null){
            final User userResponsible = userRepository.findById(patchTaskDto.getResponsible()).get();

            task.setResponsible(userResponsible);
        }

        if(patchTaskDto.getObservers() != null){
            final List<User> users = userRepository.findAllById(patchTaskDto.getObservers());

            task.setObservers(users);
        }

        if(patchTaskDto.getDueDate() != null){
            task.setDueDate(patchTaskDto.getDueDate());
        }

        if(patchTaskDto.getStatus() != null){
            final TaskStatus status = taskStatusRepository.findById(patchTaskDto.getStatus()).get();

            task.setStatus(status);
        }

        return tasksRepository.save(task);
    }

    public Task delete(@NotNull DeleteTaskDto deleteTaskDto) {
        log.info(SwaggerConst.Tasks.DELETE_TASK);

        final Task task = tasksRepository.findById(deleteTaskDto.getId()).get();

        task.setDeleteStatus(true);

        tasksRepository.save(task);

        return task;
    }

    public Comment makeComment(
            @NotNull CommentDto commentDto,
            @NotNull User user
    ) {
        Task task = tasksRepository.findById(commentDto.getTaskId()).get();

        return commentRepository.save(Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .task(task)
                .build());
    }
}
