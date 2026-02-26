package com.example.IdentifyUser.dto.request;

import com.example.IdentifyUser.Validator.DobConstraint;
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
public class UserCreationRequest {
    @NonNull
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @NonNull
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String lastName;
    String firstName;
    @DobConstraint(min = 16, message = "DOB_INVALID")
    LocalDate dob;
}
