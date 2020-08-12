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

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
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

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .lastVisit(user.getLastVisit())
                .build();
    }

    public static List<RoleDto> listRoles(List<Role> roles) {
        List<RoleDto> result = new ArrayList<>();

        roles.forEach(role -> {
            result.add(RoleDto.builder()
                    .id(role.getId())
                    .role(role.getRole())
                    .build());
        });

        return result;
    }
}
