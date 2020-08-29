package com.aegis.crmsystem.controllers.v1.ws;

import com.aegis.crmsystem.Gggg;
import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.*;
import com.aegis.crmsystem.models.Comment;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.TaskStatus;
import com.aegis.crmsystem.security.jwt.JwtUser;
import com.aegis.crmsystem.servies.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class WSTaskController {

    @Autowired
    private TaskService taskService;

    @MessageMapping("/getAllTask")
    @SendTo("/task/getAll")
    @JsonView({Views.Message.class})
    public List<Task> getAll(@Payload(required = false) FilterGetAllTaskDto filterGetAllTaskDto) {
        if(filterGetAllTaskDto == null){
            filterGetAllTaskDto = new FilterGetAllTaskDto();
        }

        return taskService.findAll(Gggg.user.getUser(), filterGetAllTaskDto);
    }

    @MessageMapping("/getAllStatus")
    @SendTo("/status/getAll")
    @JsonView({Views.Message.class})
    public List<TaskStatus> getAllStatus() {
        return taskService.findAllStatus();
    }

    @MessageMapping("/detailsTask")
    @SendTo("/task/getOne")
    @JsonView({Views.FullMessage.class})
    public Task getOne(@Payload DetailsTaskDto detailsTaskDto) {
        return taskService.getOne(detailsTaskDto, Gggg.user.getUser());
    }

    @MessageMapping("/createTask")
    @SendTo("/task/create")
    @JsonView({Views.Message.class})
    public Task create(@Payload CreateTaskDto createTaskDto) {
        return taskService.create(createTaskDto, Gggg.user.getUser());
    }

    @MessageMapping("/updateTask")
    @SendTo("/task/update")
    @JsonView({Views.Message.class})
    public Task update(@Payload UpdateTaskDto updateTaskDto) {
        return taskService.put(updateTaskDto, Gggg.user.getUser());
    }

    @MessageMapping("/patchTask")
    @SendTo("/task/patch")
    @JsonView({Views.Message.class})
    public Task patch(@Payload(required = false) PatchTaskDto patchTaskDto) {
        if(patchTaskDto == null){
            patchTaskDto = new PatchTaskDto();
        }

        return taskService.patch(patchTaskDto, Gggg.user.getUser());
    }

    @MessageMapping("/deleteTask")
    @SendTo("/task/delete")
    @JsonView({Views.Message.class})
    public Task delete(@Payload DeleteTaskDto deleteTaskDto) {
        return taskService.delete(deleteTaskDto);
    }

    @MessageMapping("/makeComment")
    @SendTo("/task/makeComment")
    @JsonView({Views.Message.class})
    public Comment makeComment(
            @Payload CommentDto commentDto
    ) {
        return taskService.makeComment(commentDto, Gggg.user.getUser());
    }
}
