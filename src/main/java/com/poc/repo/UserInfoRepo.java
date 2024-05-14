package com.poc.repo;

import com.poc.entities.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepo extends JpaRepository<UserInformation, Integer> {
    Optional<UserInformation> findByUserName(String username);
    Optional<UserInformation> findByEmail(String email);
}
