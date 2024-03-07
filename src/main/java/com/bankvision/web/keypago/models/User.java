package com.bankvision.web.keypago.models;

import java.util.Date;

import lombok.Data;

@Data
public class User {
    private String id;
    private String identificationNumber;
    private String identificationType;
    private Date dateOfBirth;
    private int age;
    private String phone;
    private String email;

   
}
