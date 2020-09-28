package com.aegis.crmsystem.controllers.v1.http;


import com.aegis.crmsystem.Auth;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @JsonView({Views.Message.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_ALL_TASKS)
    public List<Task> getAll(@RequestBody(required = false) FilterGetAllTaskDto filterGetAllTaskDto) {
        return taskService.findAll(Auth.jwtUser.getUser(), filterGetAllTaskDto);
    }

    @JsonView({Views.Message.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_ALL_TASK_STATUS)
    public List<TaskStatus> getAllStatus() {
        return taskService.findAllStatus();
    }

    @JsonView({Views.FullMessage.class})
    @ApiOperation(value = SwaggerConst.Tasks.GET_TASK_BY_ID)
    public void getOne(@RequestBody DetailsTaskDto detailsTaskDto) { }

    @JsonView({Views.Message.class})
    @PostMapping
    @ApiOperation(value = SwaggerConst.Tasks.CREATE_TASK)
    public Task create(@RequestBody CreateTaskDto createTaskDto) {
        return taskService.create(createTaskDto, Auth.jwtUser.getUser());
    }

    /**
     * @param jwtUser Текущий авторизованный пользователь
     * @param title Заголовок задачи
     * @param text Текст задачи
     * @param dueDate Дата окончания задачи
     * @param responsible Ответственный за задачу
     * @param observers Наблюдатели за задачей
     */
    @JsonView({Views.Message.class})
    @PutMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.UPDATE_TASK)
    public void update(
            @AuthenticationPrincipal JwtUser jwtUser,
            @PathVariable("id") Task task,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "text") String text,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("dueDate") Date dueDate,
            @RequestParam(value = "responsible_id") Long responsible,
            @RequestParam(value = "observers_ids", required = false) List<Long> observers
    ) {}

    /**
     * @param jwtUser Текущий авторизованный пользователь
     * @param title Заголовок задачи
     * @param text Текст задачи
     * @param dueDate Дата окончания задачи
     * @param responsible Ответственный за задачу
     * @param observers Наблюдатели за задачей
     */
    @JsonView({Views.Message.class})
    @PatchMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.UPDATE_TASK)
    public void patch(
            @AuthenticationPrincipal JwtUser jwtUser,
            @PathVariable("id") Task task,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "dueDate", required = false) Date dueDate,
            @RequestParam(value = "responsible_id", required = false) Long responsible,
            @RequestParam(value = "observers_ids", required = false) List<Long> observers
            ) {}

    @JsonView({Views.Message.class})
    @DeleteMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.DELETE_TASK)
    public void delete(@PathVariable("id") Task task) { }

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
