package com.example.IdentifyUser.Repository;


import com.example.IdentifyUser.Entity.Permission;
import com.example.IdentifyUser.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
