package com.aegis.crmsystem.dto.request.users;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class PutUserDto {
    @NotNull
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;
}
