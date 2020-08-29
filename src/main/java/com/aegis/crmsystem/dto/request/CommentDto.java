package com.aegis.crmsystem.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CommentDto {
    @NonNull
    private Long taskId;

    @NonNull
    private String text;
}
