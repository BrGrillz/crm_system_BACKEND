package com.aegis.crmsystem.dto.request.task;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class PatchTaskDto {
    private Long id;
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date dueDate;
    private Long responsible;
    private List<Long> observers;
    private Long status;
}
