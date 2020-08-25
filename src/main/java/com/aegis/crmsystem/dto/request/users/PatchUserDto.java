package com.aegis.crmsystem.dto.request.users;

import lombok.Data;
import lombok.NonNull;

@Data
public class PatchUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
