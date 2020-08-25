package com.aegis.crmsystem.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CommentDto {
    @NonNull
    private Integer taskId;

    @NonNull
    private String text;
}
