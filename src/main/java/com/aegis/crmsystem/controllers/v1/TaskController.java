package com.aegis.crmsystem.controllers.v1;


import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.models.Task;
import com.aegis.crmsystem.servies.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.aegis.crmsystem.constants.SwaggerConst;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/tasks")
@Api(value="onlinestore", tags=SwaggerConst.Tasks.API_TITLE)
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
})
public class TaskController {

    @Autowired
    private TaskService taskService;

    @JsonView(Views.Message.class)
    @GetMapping
    @ApiOperation(value = SwaggerConst.Tasks.GET_ALL_TASKS)
    public List<Task> getAll() {
        return taskService.findAll();
    }

    @JsonView(Views.Message.class)
    @GetMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.GET_TASK_BY_ID)
    public Task getOne(@PathVariable("id") Task task) {
        return taskService.getById(task);
    }

    /**
     * @param title Заголовок задачи
     * @param text Текст задачи
     */
    @JsonView(Views.FullMessage.class)
    @PostMapping
    @ApiOperation(value = SwaggerConst.Tasks.CREATE_TASK)
    public Task create(
            @RequestParam("title") String title,
            @RequestParam("text") String text
    ) {
        return taskService.create(title, text);
    }

    /**
     * @param title Заголовок задачи
     * @param text Текст задачи
     */
    @JsonView(Views.FullMessage.class)
    @PutMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.UPDATE_TASK)
    public Task update(
            @PathVariable("id") Task task,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text
    ) {

        return taskService.update(task, title, text);
    }

    @JsonView(Views.FullMessage.class)
    @DeleteMapping("{id}")
    @ApiOperation(value = SwaggerConst.Tasks.DELETE_TASK)
    public void delete(@PathVariable("id") Task task) {
        taskService.delete(task);
    }
}
