package com.myecom.myecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String fullName;
    private String mobileNumber;
    private String email;
    private String street;
    private String area;
    private String city;
    private String state;
    private String pinCode;
    private String password;

    private String role;
}
