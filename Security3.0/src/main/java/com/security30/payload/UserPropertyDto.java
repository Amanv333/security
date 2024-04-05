package com.security30.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPropertyDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userRole;
    private String userName;
}
