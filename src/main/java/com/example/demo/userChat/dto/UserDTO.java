package com.example.demo.userChat.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;

    public UserDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDTO() {
    }
}
