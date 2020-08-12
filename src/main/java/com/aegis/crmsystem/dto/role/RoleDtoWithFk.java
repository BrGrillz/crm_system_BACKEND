package com.aegis.crmsystem.dto.role;

import com.aegis.crmsystem.dto.user.UserDto;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDtoWithFk {
    private Long id;
    private String role;
    private List<UserDto> users;

    public Role toRole(){
        return Role.builder()
                .id(id)
                .role(role)
                .build();
    }

    public static RoleDtoWithFk fromRole(Role role) {
        List<User> users = role.getUsers();
        List<UserDto> userDtos = new ArrayList<>();

        users.forEach(user -> userDtos.add(UserDto.fromUser(user)));

        return RoleDtoWithFk.builder()
                .id(role.getId())
                .role(role.getRole())
                .users(userDtos)
                .build();
    }
}
