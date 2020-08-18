package com.aegis.crmsystem.controllers.v1;

import com.aegis.crmsystem.models.File;
import com.aegis.crmsystem.repositories.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;


@Slf4j
@RestController
public class UploadController {

    @Autowired
    private FileRepository fileRepository;

    private static String UPLOADED_FOLDER = "src/main/resources/documents/";

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private void saveUploadedFile(MultipartFile file) throws IOException {
        Instant instant = Instant.now();
        long timeStampSeconds = instant.getEpochSecond();
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + timeStampSeconds + "_" + file.getOriginalFilename());
            fileRepository.save(File.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(path.toString())
                    .build());
            Files.write(path, bytes);
        }
    }

    @PostMapping(value = "/api/v1/milti-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity multiUpload(@RequestParam("files") MultipartFile[] files) {
        List<Object> downloadFiles = new ArrayList<>();
        Arrays.stream(files)
                .forEach(file -> {
                    try {
                        saveUploadedFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return ResponseEntity.ok(downloadFiles);
    }
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception exception)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        if (exception instanceof MaxUploadSizeExceededException)
        {
            model.put("errors", exception.getMessage());
        } else
        {
            model.put("errors", "Unexpected error: " + exception.getMessage());
        }
        model.put("uploadedFile", new File());
        return new ModelAndView("/upload", model);
    }
}