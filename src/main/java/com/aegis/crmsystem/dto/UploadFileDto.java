package com.aegis.crmsystem.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadFileDto {
    private MultipartFile File;
}
