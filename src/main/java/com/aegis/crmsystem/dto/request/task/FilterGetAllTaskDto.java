package com.aegis.crmsystem.dto.request.task;

import lombok.Data;

@Data
public class FilterGetAllTaskDto {
    private Boolean author;
    private Boolean responsible;
    private Boolean observers;
    private Boolean deleted;
}
