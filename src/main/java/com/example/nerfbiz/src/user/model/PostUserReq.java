package com.example.nerfbiz.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {

    private String id;
    private String email;
    private String password;

}
