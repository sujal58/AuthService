package com.sujal.authservice.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class UserInfoDto {

    private String userName;

    private String password;

    private String lastName;

    private Long phoneNumber;

    private String email;
}
