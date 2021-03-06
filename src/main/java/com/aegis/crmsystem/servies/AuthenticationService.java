package com.aegis.crmsystem.servies;

import com.aegis.crmsystem.Auth;
import com.aegis.crmsystem.dto.AuthenticationRequestDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionUnauthorized;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsersService userService;

    public Map<Object, Object> getNewTokensByRefresh(String token) {
        if(jwtTokenProvider.validateRefreshToken(token)){
            final String email = jwtTokenProvider.getUserEmailFromRefreshToken(token);
            final User user = userService.findByEmail(email);

            return jwtTokenProvider.createTokens(user);
        } else {
            throw new ApiRequestExceptionUnauthorized("Реврешь токен уже все");
        }
    }

    public Map<Object, Object> login(AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();

        User user = userService.findByEmail(email);
        if(user == null){
            throw new ApiRequestExceptionNotFound("Email - Такого адресса электронной почты не существует");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        } catch (Exception e){
            throw new ApiRequestExceptionNotFound("Password - Неверный пароль");
        }

        Auth.user = user;

        return jwtTokenProvider.createTokens(user);
    }
}
