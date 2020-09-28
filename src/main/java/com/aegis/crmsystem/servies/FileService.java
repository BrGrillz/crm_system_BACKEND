package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.models.File;
import com.aegis.crmsystem.properties.StorageProperties;
import com.aegis.crmsystem.repositories.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final Path rootLocation;

    @Autowired
    public FileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
//            throw new StorageException("Could not initialize storage location", e);
        }
    }

    public File store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        try {
            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file " + filename);
                return null;
            }
            if (filename.contains("..")) {
                // This is a security check
//                throw new StorageException(
//                        "Cannot store file with relative path outside current directory "
//                                + filename);
                return null;
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(resultFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
//            throw new StorageException("Failed to store file " + filename, e);
            return null;
        }

        String download = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/download/")
                .path(resultFilename)
                .toUriString();

        Path path = load(resultFilename);
        java.io.File currentFile = path.toFile();
        String mimeType = null;

        Long test = file.getSize();

        try{
            mimeType = Files.probeContentType(currentFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileRepository.save(File.builder()
                .name(file.getOriginalFilename())
                .uuid(resultFilename)
                .download(download)
                .size(file.getSize())
                .path(currentFile.getAbsolutePath())
                .format(mimeType)
                .build());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
//            throw new StorageException("Failed to read stored files", e);
            return null;
        }

    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public File getFileByUuid(String uuidFileName){
        return fileRepository.findByUuid(uuidFileName);
    }

    public Resource loadAsResource(String uuidFileName) {
        try {
            Path filePath = load(uuidFileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                return null;
//                throw new FileNotFoundException(
//                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            return null;
//            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
