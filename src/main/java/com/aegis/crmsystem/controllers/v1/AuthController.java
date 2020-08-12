package com.aegis.crmsystem.controllers.v1;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.AuthenticationRequestDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionNotFound;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionUnauthorized;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.security.jwt.JwtTokenProvider;
import com.aegis.crmsystem.servies.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth/")
//@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") }, tags= SwaggerConst.Auth.API_TITLE)
@Api(tags= SwaggerConst.Auth.API_TITLE)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ApiOperation(value = SwaggerConst.Auth.LOGIN)
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new ApiRequestExceptionNotFound("User with username: " + email + " not found");
        }

        return ResponseEntity.ok(jwtTokenProvider.createTokens(user));
    }

    @PostMapping("refresh")
    @ApiOperation(value = SwaggerConst.Auth.REFRESH_TOKEN)
    public ResponseEntity getNewTokensByRefresh(@RequestParam String token) {
        if(jwtTokenProvider.validateRefreshToken(token)){
            final String email = jwtTokenProvider.getUserEmailFromRefreshToken(token);
            final User user = userService.findByEmail(email);

            return ResponseEntity.ok(jwtTokenProvider.createTokens(user));
        } else {
            throw new ApiRequestExceptionUnauthorized("Реврешь токен уже все");
        }
    }

    @PostMapping("verify")
    @ApiOperation(value = SwaggerConst.Auth.VERIFY_TOKEN)
    public ResponseEntity checkAccessToken(@RequestParam String token) {
        return ResponseEntity.ok(jwtTokenProvider.validateAccessToken(token));
    }
}
