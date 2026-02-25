package com.example.IdentifyUser.Repository;


import com.example.IdentifyUser.Entity.Permission;
import com.example.IdentifyUser.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
