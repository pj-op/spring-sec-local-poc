package com.poc.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInformation {

    public UserInformation(String userName, String password, String email, String roles){
        this.userName=userName;
        this.password=password;
        this.email=email;
        this.roles=roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    private String email;
    private String roles;
}
