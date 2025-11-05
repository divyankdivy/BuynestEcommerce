package com.myecom.myecomapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String fullName;
    private String email;
    private String mobileNumber;
    private String street;
    private String area;
    private String city;
    private String state;
    private String pinCode;
}
