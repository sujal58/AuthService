package com.sujal.authservice.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
public class UserInfoDto {

    private String username;

    private String password;

    private String lastName;

    private Long phoneNumber;

    private String email;
}
