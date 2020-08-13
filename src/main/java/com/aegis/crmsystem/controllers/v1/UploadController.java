package com.aegis.crmsystem.controllers.v1;

import com.aegis.crmsystem.dto.UploadFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private static String UPLOADED_FOLDER = "E:\\crm_system_BACKEND\\src\\main\\java\\com\\aegis\\crmsystem\\documents";

//    @PostMapping(value = "/api/v1/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Object> uploadFile(@RequestParam("files") MultipartFile[] files, @ModelAttribute UploadFileDto model) throws IOException {
//        logger.debug("Multiple file upload! {}", files);
//        if (files != null && files.length > 0) {
//            for (MultipartFile multipartFile : files) {
//                try {
//                    if (!(multipartFile.isEmpty())) {
//                        String fileName = multipartFile.getOriginalFilename();
//                        byte[] bytes = multipartFile.getBytes();
//                        Path path = Paths.get(UPLOADED_FOLDER + fileName);
//                        Files.write(path, bytes);
//                        // Call the save to DB or disk here.
//                    }
//                } catch (Exception e) {
//                    return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
//                }
//            }
//        }
//        return new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
//    }

    @PostMapping(value = "/api/v1/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadFileDto model) {
        try {
            // Save as you want as per requiremens
            saveUploadedFile(model.getFile());

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
    }

    private void saveUploadedFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }
}