package com.aegis.crmsystem.controllers.v1.http;

import com.aegis.crmsystem.Auth;
import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.models.File;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.UserRepository;
import com.aegis.crmsystem.servies.FileService;
import com.aegis.crmsystem.servies.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping("avatar")
    public User changeAvatar(
            @RequestParam("file") MultipartFile file
    ){
        File avatar = fileService.store(file);
        User user = Auth.user;
        user.setAvatar(avatar);

        return userRepository.save(user);
    }
}
