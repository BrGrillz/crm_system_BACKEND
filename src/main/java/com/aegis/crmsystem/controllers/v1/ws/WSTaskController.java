package com.aegis.crmsystem.controllers.v1.ws;

import com.aegis.crmsystem.Auth;
import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.*;
import com.aegis.crmsystem.dto.role.RoleDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.models.Comment;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.TaskStatus;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.servies.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class WSTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/getAllTask")
//    @SendTo("/task/getAll")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView({Views.Message.class})
    public void getAll(
            @Payload(required = false) FilterGetAllTaskDto filterGetAllTaskDto
    ) {
        final List<Task> listTasks = taskService.findAll(Auth.jwtUser.getUser(), filterGetAllTaskDto);

        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "/task/getAll",
                listTasks
        );

        throw new ApiRequestExceptionNotFound("Задача по такому id не найден");
    }

    @MessageMapping("/getAllStatus")
    @JsonView({Views.Message.class})
    public void getAllStatus() {
        List<TaskStatus> taskStatusList = taskService.findAllStatus();

        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "/status/getAll",
                taskStatusList
        );
    }

    @MessageMapping("/task/details")
    @JsonView({Views.FullMessage.class})
    public void getDetails(
            @Payload DetailsTaskDto detailsTaskDto
    ) {
        final Task task = taskService.getOne(detailsTaskDto, Auth.jwtUser.getUser());

        simpMessagingTemplate.convertAndSend(
                "/task/" + detailsTaskDto.getId() + "/details",
                task
        );
    }

    @MessageMapping("/createTask")
    @JsonView({Views.Message.class})
    public void create(@Payload CreateTaskDto createTaskDto) {
        final Task task = taskService.create(createTaskDto, Auth.jwtUser.getUser());

        taskService.sendToUsers(task, "create");
        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "response/task/create/success",
                task
        );
    }

    @MessageMapping("/updateTask")
    @JsonView({Views.Message.class})
    public void update(@Payload UpdateTaskDto updateTaskDto) {
        final Task task = taskService.put(updateTaskDto, Auth.jwtUser.getUser());

        taskService.sendToUsers(task, "update");
        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "response/task/update/success",
                task
        );
    }

    @MessageMapping("/patchTask")
    @JsonView({Views.Message.class})
    public void patch(@Payload(required = false) PatchTaskDto patchTaskDto) {
        final Task task = taskService.patch(patchTaskDto, Auth.jwtUser.getUser());

        taskService.sendToUsers(task, "patch");
        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "response/task/patch/success",
                task
        );
    }

    @MessageMapping("/deleteTask")
    @JsonView({Views.Message.class})
    public void delete(@Payload DeleteTaskDto deleteTaskDto) {
        final Task task = taskService.delete(deleteTaskDto);

        taskService.sendToUsers(task, "delete");
        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "response/task/delete/success",
                task
        );
    }

    @MessageMapping("/makeComment")
    @JsonView({Views.Message.class})
    public void makeComment(
            @Payload CommentDto commentDto
    ) {
        final Comment comment = taskService.makeComment(commentDto, Auth.jwtUser.getUser());

        simpMessagingTemplate.convertAndSend(
                "/task/" + commentDto.getTaskId() + "/comments",
                comment
        );
        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "response/task/comment/success",
                comment
        );

    }

    @MessageExceptionHandler
    public void handleException(IllegalArgumentException exception) {

        simpMessagingTemplate.convertAndSendToUser(
                Auth.jwtUser.getUser().getEmail(),
                "/task/error",
                exception.getMessage()
        );
    }
}
