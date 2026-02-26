package com.example.IdentifyUser.Repository;


import com.example.IdentifyUser.Entity.InvalidateToken;
import com.example.IdentifyUser.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {
}
