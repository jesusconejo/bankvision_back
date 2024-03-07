package com.bankvision.web.keypago.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserRequest {
    private String identificationNumber;
    private String identificationType;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private String password;
}
