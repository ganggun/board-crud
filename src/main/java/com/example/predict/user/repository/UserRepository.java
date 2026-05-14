package com.example.predict.user.repository;

import com.example.predict.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPublicId(String publicId);

    Optional<User> findByStudentId(String studentId);
}
