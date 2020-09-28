package com.aegis.crmsystem.dto.request;

import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CommentDto {
    @NonNull
    private Long taskId;

    @NonNull
    private String text;

    private List<Long> files;
}
