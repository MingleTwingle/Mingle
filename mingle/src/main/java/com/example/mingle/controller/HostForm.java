package com.example.mingle.controller;

import com.example.mingle.domain.Host;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostForm {
    private String host_name;
    private String host_idid;
    private String host_nickname;
    private String host_password;
    private String host_email;
    private String host_phone_number;
    private String host_gender;
    private String host_type;
}
