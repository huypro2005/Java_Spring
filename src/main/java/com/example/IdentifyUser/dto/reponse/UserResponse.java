package com.example.IdentifyUser.dto.reponse;

import com.example.IdentifyUser.Entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
//    String password;
    String lastName;
    String firstName;
    LocalDate dob;
    Set<Role> roles;
}
