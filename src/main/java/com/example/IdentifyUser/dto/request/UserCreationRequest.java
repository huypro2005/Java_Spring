package com.example.IdentifyUser.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


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
    String dob;
}
