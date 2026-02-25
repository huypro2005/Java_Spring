package com.example.IdentifyUser.dto.reponse;


import com.example.IdentifyUser.Entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;
    List<Permission> permissions;
}
