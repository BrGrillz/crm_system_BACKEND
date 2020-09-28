package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.*;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionAccessDenied;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.models.*;
import com.aegis.crmsystem.repositories.*;
import com.aegis.crmsystem.specifications.TaskSpecification;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MailService mailService;

    public Task create(
            @NotNull CreateTaskDto createTaskDto,
            @NotNull User user
    ) {
        User responsibleUser = userRepository.findById(createTaskDto.getResponsible()).get();

        List<User> observersUsers = null;
        if(createTaskDto.getObservers() != null){
            observersUsers = userRepository.findAllById(createTaskDto.getObservers());
        }

        List<File> files = null;
        if(createTaskDto.getFiles() != null){
            files = fileRepository.findAllById(createTaskDto.getFiles());
        }

        TaskStatus taskStatus = taskStatusRepository.findById(1L).get();

        final Task newTask =  tasksRepository.save(Task.builder()
                .title(createTaskDto.getTitle())
                .description(createTaskDto.getDescription())
                .dueDate(createTaskDto.getDueDate())
                .status(taskStatus)
                .author(user)
                .files(files)
                .deleteStatus(false)
                .responsible(responsibleUser)
                .observers(observersUsers)
                .build());

        final List<User> listUser = getListOfUsersRelatedTask(newTask);

        listUser.forEach(userOnList -> mailService.sendNewTaskInfo(userOnList, newTask));

        return newTask;
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
        final List<User> observers = task.getObservers();

        if(!userId.equals(authorId) && !userId.equals(responsibleId) && !userEqualsObservers(userId, observers)){
            throw new ApiRequestExceptionAccessDenied("У вас нету доступа для просмотра этой задачи");
        }

        return task;
    }

    protected Boolean userEqualsObservers(Long userId, List<User> observers){
        AtomicReference<Boolean> equals = new AtomicReference<>(false);

        observers.forEach(observer -> {
            if(userId.equals(observer.getId())){
                equals.set(true);
            }
        });

        return equals.get();
    }

    public List<Task> findAll(User user, FilterGetAllTaskDto filterGetAllTaskDto) {
        final TaskSpecification taskSpecification = new TaskSpecification(filterGetAllTaskDto, user);
        final List<Task> listTasks = tasksRepository.findAll(taskSpecification);

        return  listTasks;

//        log.info(SwaggerConst.Tasks.GET_ALL_TASKS);
//
//        List<Task> listTasks = null;
//
//        if(filterGetAllTaskDto.getAuthor() != null){
//            listTasks = tasksRepository.findWhereUserAuthor(user);
//        }
//
//        if(filterGetAllTaskDto.getResponsible() != null){
//            listTasks = tasksRepository.findWhereUserResponsible(user);
//        }
//
//        if(filterGetAllTaskDto.getObservers() != null){
//            listTasks = tasksRepository.findWhereUserObserver(user);
//        }
//
//        if(filterGetAllTaskDto.getDeleted() != null){
//            listTasks = tasksRepository.findDeleted();
//        }
//
//        if(listTasks == null){
//            listTasks = tasksRepository.findAllByUser(user);
//        }
//
//        if(filterGetAllTaskDto.getFilterByStatus() != null){
//            final TaskStatus status = taskStatusRepository.findById(filterGetAllTaskDto.getFilterByStatus()).get();
//
//            tasksRepository.filterByStatus
//        }
//
//        return tasksRepository.findAllByUser(user);
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

        List<File> listFiles = null;
        if(updateTaskDto.getFiles() != null){
            listFiles = this.fileRepository.findAllById(updateTaskDto.getFiles());
        }

        task.setTitle(updateTaskDto.getTitle());
        task.setDescription(updateTaskDto.getDescription());
        task.setResponsible(userResponsible);
        task.setObservers(userObservers);
        task.setDueDate(updateTaskDto.getDueDate());
        task.setStatus(status);
        task.setFiles(listFiles);

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

        if(patchTaskDto.getFiles() != null){
            List<File> listFiles = this.fileRepository.findAllById(patchTaskDto.getFiles());
            task.setFiles(listFiles);
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
        final Task task = tasksRepository.findById(commentDto.getTaskId()).get();
        List<File> files = null;
        
        if(commentDto.getFiles() != null){
            files = fileRepository.findAllById(commentDto.getFiles());
        }
        log.debug("commentDto ================================ {}", commentDto);

        return commentRepository.save(Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .files(files)
                .task(task)
                .build());
    }

    public void sendToUsers(Task task, String prefix){
        final List<User> users = getListOfUsersRelatedTask(task);

        users.forEach(user -> simpMessagingTemplate.convertAndSendToUser(
                user.getEmail(),"/task/" + prefix,
                task
        ));
    }

    public List<User> getListOfUsersRelatedTask(Task task){
        List<User> observers = task.getObservers();
        List<User> sendToUsers = Stream.concat(observers.stream(), new ArrayList<User>().stream())
                .collect(Collectors.toList());

        if(!userInUserList(sendToUsers, task.getAuthor())){
            sendToUsers.add(task.getAuthor());
        }

        if(!userInUserList(sendToUsers, task.getResponsible())){
            sendToUsers.add(task.getResponsible());
        }

        return sendToUsers;
    }

    protected Boolean userInUserList(List<User> users, User searchUser){
        AtomicReference<Boolean> checker = new AtomicReference<>(false);

        users.forEach((user) -> {
            if(user.getId().equals(searchUser.getId())){
                checker.set(true);
            }
        });

        return checker.get();
    }
}
