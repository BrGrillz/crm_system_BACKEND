package com.aegis.crmsystem.controllers.v1;


import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.role.RoleDto;
import com.aegis.crmsystem.dto.role.RoleDtoWithFk;
import com.aegis.crmsystem.dto.user.UserDto;
import com.aegis.crmsystem.dto.user.UserDtoWithFk;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.servies.RoleService;
import com.aegis.crmsystem.servies.UserService;
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
@RequestMapping("api/v1/admin")
@Api(tags= SwaggerConst.Admin.API_TITLE)
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    @ApiOperation(value = SwaggerConst.Admin.ALL_USERS)
    public List<UserDtoWithFk> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/role")
    @ApiOperation(value = SwaggerConst.Admin.ALL_ROLE)
    public List<RoleDto> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/role/{id}")
    @ApiOperation(value = SwaggerConst.Admin.ALL_USERS_BY_ROLE)
    public RoleDtoWithFk getAllUsersByRole(@PathVariable("id") Long id) {
        return roleService.getById(id);
    }
}
