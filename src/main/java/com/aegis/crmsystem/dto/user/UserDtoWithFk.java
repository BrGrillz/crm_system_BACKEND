package com.aegis.crmsystem.dto.user;

import com.aegis.crmsystem.dto.role.RoleDto;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDtoWithFk {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDto> roles;
    private LocalDateTime lastVisit;

    public User toUser(){
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastVisit(lastVisit)
                .build();
    }

    public static UserDtoWithFk fromUser(User user) {
        List<Role> roles = user.getRoles();
        List<RoleDto> rolesDto = new ArrayList<>();

        roles.forEach(role -> rolesDto.add(RoleDto.fromRole(role)));

        return UserDtoWithFk.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(rolesDto)
                .lastVisit(user.getLastVisit())
                .build();
    }
}
