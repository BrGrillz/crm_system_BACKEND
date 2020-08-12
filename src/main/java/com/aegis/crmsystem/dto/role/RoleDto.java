package com.aegis.crmsystem.dto.role;

import com.aegis.crmsystem.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {
    private Long id;
    private String role;

    public Role toRole(){
        return Role.builder()
                .id(id)
                .role(role)
                .build();
    }

    public static RoleDto fromRole(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .role(role.getRole())
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
