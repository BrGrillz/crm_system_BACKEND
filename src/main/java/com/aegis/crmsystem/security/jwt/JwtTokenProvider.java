package com.aegis.crmsystem.security.jwt;

import com.aegis.crmsystem.exceptions.ApiRequestExceptionBadRequest;
import com.aegis.crmsystem.models.Role;
import com.aegis.crmsystem.models.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.access_token}")
    private String secretAccessToken;

    @Value("${jwt.token.refresh_token}")
    private String secretRefreshToken;

    @Value("${jwt.token.access_token.expired}")
    private long validityAccessTokenInMilliseconds;

    @Value("${jwt.token.refresh_token.expired}")
    private long validityRefreshTokenInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secretAccessToken = Base64.getEncoder().encodeToString(secretAccessToken.getBytes());
        secretRefreshToken = Base64.getEncoder().encodeToString(secretRefreshToken.getBytes());
    }

    public Map<Object, Object> createTokens(User user) {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", getRoleNames(user.getRoles()));

        Date now = new Date();
        Date validityForAccessToken = new Date(now.getTime() + validityAccessTokenInMilliseconds);
        Date validityForRefreshToken = new Date(now.getTime() + validityRefreshTokenInMilliseconds);

        Map<Object, Object> response = new HashMap<>();

        response.put("access_token", Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validityForAccessToken)
                .signWith(SignatureAlgorithm.HS256, secretAccessToken)
                .compact());
        response.put("refresh_token", Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validityForRefreshToken)
                .signWith(SignatureAlgorithm.HS256, secretRefreshToken)
                .compact());

        return response;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmailFromAccessToken(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmailFromAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretAccessToken).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserEmailFromRefreshToken(String token) {
        return Jwts.parser().setSigningKey(secretRefreshToken).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
//        else {
//            throw new ApiRequestExceptionUnauthorized("Вы не авторизованны");
//        }
    }

    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretAccessToken).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiRequestExceptionBadRequest("access token is invalid");
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretRefreshToken).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiRequestExceptionBadRequest("refresh token is invalid");
        }
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getRole());
        });

        return result;
    }
}
