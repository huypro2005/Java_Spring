package com.example.IdentifyUser.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    @Size(min = 3, max = 20, message = "Permission name must be between 3 and 20 characters")
    String name;

    @Size(max = 100, message = "Permission description must be less than 100 characters")
    String description;
}
