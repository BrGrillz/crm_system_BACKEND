package com.aegis.crmsystem.controllers.v1;


import com.aegis.crmsystem.Gggg;
import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.dto.request.CommentDto;
import com.aegis.crmsystem.dto.request.task.CreateTaskDto;
import com.aegis.crmsystem.dto.request.task.DetailsTaskDto;
import com.aegis.crmsystem.dto.request.task.FilterGetAllTaskDto;
import com.aegis.crmsystem.models.Comment;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.models.TaskStatus;
import com.aegis.crmsystem.security.jwt.JwtUser;
import com.aegis.crmsystem.servies.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.Nullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/tasks")
@Api(value="onlinestore", tags=SwaggerConst.Tasks.API_TITLE)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @MessageMapping("/getAllTask")
    @SendTo("/task/getAll")
    @JsonView({Views.Message.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_ALL_TASKS)
    public List<Task> getAll(@Nullable FilterGetAllTaskDto filterGetAllTaskDto) {
        if(filterGetAllTaskDto == null){
            filterGetAllTaskDto = new FilterGetAllTaskDto();
        }

        return taskService.findAll(Gggg.user.getUser(), filterGetAllTaskDto);
    }

    @MessageMapping("/getAllStatus")
    @SendTo("/status/getAll")
    @JsonView({Views.Message.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_ALL_TASK_STATUS)
    public List<TaskStatus> getAllStatus() {
        return taskService.findAllStatus();
    }

    @MessageMapping("/detailsTask")
    @SendTo("/task/getOne")
    @JsonView({Views.FullMessage.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_TASK_BY_ID)
    public Task getOne(
            @RequestBody DetailsTaskDto detailsTaskDto
            ) {
        return taskService.getById(detailsTaskDto, Gggg.user.getUser());
    }

    @MessageMapping("/createTask")
    @SendTo("/task/create")
    @JsonView({Views.Message.class})
    @PostMapping
    @ApiOperation(value = SwaggerConst.Tasks.CREATE_TASK)
    public Task create(@RequestBody CreateTaskDto createTaskDto) {
        return taskService.create(createTaskDto, Gggg.user.getUser());
    }

    /**
     * @param jwtUser Текущий авторизованный пользователь
     * @param title Заголовок задачи
     * @param text Текст задачи
     * @param dueDate Дата окончания задачи
     * @param responsible Ответственный за задачу
     * @param observers Наблюдатели за задачей
     */
    @MessageMapping("/updateTask")
    @SendTo("/task/update")
    @JsonView({Views.Message.class})
    @PutMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.UPDATE_TASK)
    public Task update(
            @AuthenticationPrincipal JwtUser jwtUser,
            @PathVariable("id") Task task,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "text") String text,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("dueDate") Date dueDate,
            @RequestParam(value = "responsible_id") Long responsible,
            @RequestParam(value = "observers_ids", required = false) List<Long> observers
    ) {
        return taskService.put(task, title, text, responsible, observers, dueDate, jwtUser.getUser());
    }

    /**
     * @param jwtUser Текущий авторизованный пользователь
     * @param title Заголовок задачи
     * @param text Текст задачи
     * @param dueDate Дата окончания задачи
     * @param responsible Ответственный за задачу
     * @param observers Наблюдатели за задачей
     */
    @MessageMapping("/patchTask")
    @SendTo("/task/patch")
    @JsonView({Views.Message.class})
    @PatchMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.UPDATE_TASK)
    public Task patch(
            @AuthenticationPrincipal JwtUser jwtUser,
            @PathVariable("id") Task task,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "dueDate", required = false) Date dueDate,
            @RequestParam(value = "responsible_id", required = false) Long responsible,
            @RequestParam(value = "observers_ids", required = false) List<Long> observers
            ) {
        return taskService.patch(task, title, text, responsible, observers, dueDate, jwtUser.getUser());
    }

    @MessageMapping("/deleteTask")
    @SendTo("/task/delete")
    @JsonView({Views.Message.class})
    @DeleteMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.DELETE_TASK)
    public void delete(@PathVariable("id") Task task) {
        taskService.delete(task);
    }

    @MessageMapping("/commentTask")
    @SendTo("/task/makeComment")
    @JsonView({Views.Message.class})
    @PostMapping("comment")
    @ApiOperation(value = SwaggerConst.Tasks.CREATE_COMMENT)
    public Comment makeComment(
            @AuthenticationPrincipal JwtUser jwtUser,
            @RequestBody CommentDto commentDto
    ) {
        return taskService.makeComment(commentDto, jwtUser.getUser());
    }
}
