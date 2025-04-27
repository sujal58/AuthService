package com.sujal.authservice.controller;

import com.sujal.authservice.entities.RefreshToken;
import com.sujal.authservice.request.AuthRequestDto;
import com.sujal.authservice.request.RefreshTokenReqDto;
import com.sujal.authservice.response.AuthResponseDto;
import com.sujal.authservice.service.JwtService;
import com.sujal.authservice.service.RefreshTokenService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;

@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("auth/v1/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());

            return new ResponseEntity<>(AuthResponseDto.builder().accessToken(jwtService
                    .GenerateToken(authRequestDto.getUsername()))
                    .token(refreshToken.getToken())
                    .build(),
                    HttpStatus.OK
            );
        }else{
            return new ResponseEntity<>("Error while login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/auth/v1/refresh-token")
    public AuthResponseDto refreshToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto){
        return refreshTokenService.findByToken(refreshTokenReqDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                            String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                            return AuthResponseDto.builder()
                                    .accessToken(accessToken)
                                    .token(refreshTokenReqDto.getToken()).build();
                        }).orElseThrow(()-> new RuntimeException("Refresh token not found...."));
    }

    @GetMapping("/secretkey")
    public Key generateSecretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}
