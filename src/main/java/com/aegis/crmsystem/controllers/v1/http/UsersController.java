package com.aegis.crmsystem.controllers.v1.http;


import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.dto.role.RoleDto;
import com.aegis.crmsystem.dto.role.RoleDtoWithFk;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.servies.RoleService;
import com.aegis.crmsystem.servies.UsersService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/users")
@Api(tags= SwaggerConst.User.API_TITLE)
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoleService roleService;

    @JsonView({Views.Message.class})
    @ApiOperation(value = SwaggerConst.User.ALL_USERS)

    @GetMapping
    public List<User> getAllUsers() {
        return usersService.findAll();
    }

    @GetMapping("/role")
    @ApiOperation(value = SwaggerConst.User.ALL_ROLE)
    public List<RoleDto> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/role/{id}")
    @ApiOperation(value = SwaggerConst.User.ALL_USERS_BY_ROLE)
    public RoleDtoWithFk getAllUsersByRole(@PathVariable("id") Long id) {
        return roleService.getById(id);
    }
}
