package com.aegis.crmsystem.controllers.v1.ws;

import com.aegis.crmsystem.dto.request.users.CreateUserDto;
import com.aegis.crmsystem.dto.request.users.DeleteUserDto;
import com.aegis.crmsystem.dto.request.users.PatchUserDto;
import com.aegis.crmsystem.dto.request.users.PutUserDto;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.servies.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
public class WSAdminController {
    @Autowired
    private UserService userService;

    @MessageMapping("/createUser")
    @SendTo("/user/create")
    public User createUser(@Payload CreateUserDto createUserDto) {
        return userService.create(createUserDto);
    }

    @MessageMapping("/deleteUser")
    @SendTo("/user/delete")
    public User deleteUser(@Payload DeleteUserDto deleteUserDto) {
        return userService.delete(deleteUserDto);
    }

    @MessageMapping("/putUser")
    @SendTo("/user/update")
    public User updateUser(@Payload PutUserDto patchUserDto) {
        return userService.put(patchUserDto);
    }

    @MessageMapping("/patchUser")
    @SendTo("/user/update")
    public User patchUser(@Payload PatchUserDto patchUserDto) {
        return userService.patch(patchUserDto);
    }
}
