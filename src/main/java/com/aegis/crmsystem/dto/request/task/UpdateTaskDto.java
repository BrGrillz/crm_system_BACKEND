package com.aegis.crmsystem.dto.request.task;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class UpdateTaskDto {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp dueDate;

    @NotNull
    private Long responsible;

    @NotNull
    private List<Long> observers;

    @NotNull
    private Long status;

    private List<Long> files;
}
