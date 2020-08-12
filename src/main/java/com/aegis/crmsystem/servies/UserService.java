package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.dto.user.UserDto;
import com.aegis.crmsystem.dto.user.UserDtoWithFk;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDtoWithFk> findAll() {
        List<UserDtoWithFk> result = new ArrayList<>();

        List<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> {
            result.add(UserDtoWithFk.fromUser(user));
        });;

        return result;
    }

    public User findByEmail(String email) { return userRepository.findByEmail(email); }
}
