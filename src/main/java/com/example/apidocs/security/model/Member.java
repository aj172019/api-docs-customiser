package com.example.apidocs.security.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    protected Member() {
    }

    private Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Member registerUser(String username, String password) {
        return new Member(username, password);
    }
}
