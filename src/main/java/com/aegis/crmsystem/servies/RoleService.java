package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.dto.role.RoleDto;
import com.aegis.crmsystem.dto.role.RoleDtoWithFk;
import com.aegis.crmsystem.dto.user.UserDto;
import com.aegis.crmsystem.dto.user.UserDtoWithFk;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDto> findAll() {
        List<RoleDto> result = new ArrayList<>();

        List<Role> listRole = roleRepository.findAll();
        listRole.forEach(role -> {
            result.add(RoleDto.fromRole(role));
        });;

        return result;
    }

    public RoleDtoWithFk getById(Long id){
        Role role = roleRepository.findById(id).get();
        return RoleDtoWithFk.fromRole(role);
    }
}
