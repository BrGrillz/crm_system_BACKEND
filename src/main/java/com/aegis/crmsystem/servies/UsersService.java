package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.dto.request.users.CreateUserDto;
import com.aegis.crmsystem.dto.request.users.DeleteUserDto;
import com.aegis.crmsystem.dto.request.users.PatchUserDto;
import com.aegis.crmsystem.dto.request.users.PutUserDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionBadRequest;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.repositories.RoleRepository;
import com.aegis.crmsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(
            CreateUserDto createUserDto
    ) {
        final String email = createUserDto.getEmail();
        final String firstName = createUserDto.getFirstName();
        final String lastName = createUserDto.getLastName();

        final User userByEmail = userRepository.findByEmail(email);

        if(userByEmail != null){
            throw new ApiRequestExceptionBadRequest("Пользователь с таким email уже существует");
        }

        final List<Role> listRoles = roleRepository.findAllById(createUserDto.getRoleIds());

        String password = "123";

        mailService.sendAuthInfo(email, firstName, password);

        return userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .roles(listRoles)
                .build());
    }

    public User delete(DeleteUserDto deleteUserDto) {
        final User user = userRepository.findById(deleteUserDto.getId()).get();

        userRepository.deleteById(deleteUserDto.getId());

        return user;
    }

    public User put(
            PutUserDto patchUserDto
    ) {
        final User userByEmail = userRepository.findByEmail(patchUserDto.getEmail());
        if(userByEmail != null && !userByEmail.getId().equals(patchUserDto.getId())){
            throw new ApiRequestExceptionBadRequest("Пользователь с таким email уже существует");
        }

        final User user = userRepository.findById(patchUserDto.getId()).get();

        user.setFirstName(patchUserDto.getFirstName());
        user.setLastName(patchUserDto.getLastName());
        user.setEmail(patchUserDto.getEmail());

        return userRepository.save(user);
    }

    public User patch(
            PatchUserDto patchUserDto
    ) {
        final User user = userRepository.findById(patchUserDto.getId()).get();
        final String firstName = patchUserDto.getFirstName();
        final String lastName = patchUserDto.getLastName();
        final String email = patchUserDto.getEmail();

        if(firstName != null){
            user.setFirstName(firstName);
        }

        if(lastName != null){
            user.setLastName(lastName);
        }

        if(email != null){
            final User userByEmail = userRepository.findByEmail(email);
            if(userByEmail != null && !userByEmail.getId().equals(patchUserDto.getId())){
                throw new ApiRequestExceptionBadRequest("Пользователь с таким email уже существует");
            }

            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email) { return userRepository.findByEmail(email); }
}
