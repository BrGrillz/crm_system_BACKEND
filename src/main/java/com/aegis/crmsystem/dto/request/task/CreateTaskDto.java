package com.aegis.crmsystem.dto.request.task;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CreateTaskDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    @NotNull
    private Long responsible;

    private List<Long> observers;

    private List<Long> files;
}
