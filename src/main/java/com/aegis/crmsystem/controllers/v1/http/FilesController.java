package com.aegis.crmsystem.controllers.v1.http;

import com.aegis.crmsystem.models.File;
import com.aegis.crmsystem.servies.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;

import javax.activation.FileTypeMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    @PostMapping("upload")
    public File uploadFiles(
            @RequestParam("file") MultipartFile file
    ){
        return fileService.store(file);
    }

    @PostMapping("/uploads")
    @ResponseBody
    public List<File> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFiles(file))
                .collect(Collectors.toList());
    }

//    @GetMapping("/show/{uuidFileName:.+}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String uuidFileName) throws IOException{
//        final Resource resource = fileService.loadAsResource(uuidFileName);
//        java.io.File img = resource.getFile();
//        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
//    }

    @GetMapping("/download/{uuidFileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuidFileName) {
        final Resource resource = fileService.loadAsResource(uuidFileName);
        final File file = fileService.getFileByUuid(uuidFileName);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }
}
