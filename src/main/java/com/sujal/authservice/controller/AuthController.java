package com.sujal.authservice.controller;

import com.sujal.authservice.entities.RefreshToken;
import com.sujal.authservice.model.UserInfoDto;
import com.sujal.authservice.response.AuthResponseDto;
import com.sujal.authservice.service.JwtService;
import com.sujal.authservice.service.RefreshTokenService;
import com.sujal.authservice.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;


    @PostMapping("/auth/v1/signup")
    public ResponseEntity<?> signup(@RequestBody UserInfoDto user){
        try{
            Boolean isSignedUp = userDetailsServiceImp.signupUser(user);

            if(Boolean.FALSE.equals(isSignedUp)){
                return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            String jwtToken = jwtService.GenerateToken(user.getUsername());

            return new ResponseEntity<>(AuthResponseDto.builder().accessToken(jwtToken).token(refreshToken.getToken()).build(), HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>("Failed to sign up.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
