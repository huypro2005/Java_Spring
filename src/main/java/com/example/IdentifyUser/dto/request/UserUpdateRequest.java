package com.example.IdentifyUser.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    List<String> roles;
}
