package com.aegis.crmsystem.dto.request.users;
import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
public class CreateUserDto {

    @NotNull
    private List<Long> roleIds;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;
}
