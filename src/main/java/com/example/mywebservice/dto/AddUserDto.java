package com.example.mywebservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserDto {
    private String email;
    private String password;
    private String nickname;
}
