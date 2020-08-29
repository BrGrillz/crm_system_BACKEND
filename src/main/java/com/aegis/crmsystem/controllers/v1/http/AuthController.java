package com.aegis.crmsystem.controllers.v1.http;

import com.aegis.crmsystem.Gggg;
import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.dto.AuthenticationRequestDto;
import com.aegis.crmsystem.dto.request.AccessTokenDto;
import com.aegis.crmsystem.dto.request.RefreshTokenDto;
import com.aegis.crmsystem.exceptions.ApiRequestExceptionUnauthorized;
import com.aegis.crmsystem.models.User;
import com.aegis.crmsystem.security.jwt.JwtTokenProvider;
import com.aegis.crmsystem.servies.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth/")
//@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") }, tags= SwaggerConst.Auth.API_TITLE)
@Api(tags= SwaggerConst.Auth.API_TITLE)
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    @ApiOperation(value = SwaggerConst.Auth.LOGIN)
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.login(requestDto));
    }

    @PostMapping("refresh")
    @ApiOperation(value = SwaggerConst.Auth.REFRESH_TOKEN)
    public ResponseEntity getNewTokensByRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authenticationService.getNewTokensByRefresh(refreshTokenDto.getRefresh()));
    }

    @PostMapping("verify")
    @ApiOperation(value = SwaggerConst.Auth.VERIFY_TOKEN)
    public ResponseEntity checkAccessToken(@RequestBody AccessTokenDto accessTokenDto) {
        return ResponseEntity.ok(jwtTokenProvider.validateAccessToken(accessTokenDto.getToken()));
    }

    @GetMapping("user")
    public User getCurrentUser(){
        return Gggg.user.getUser();
    }
}
