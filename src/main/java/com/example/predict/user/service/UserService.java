package com.example.predict.user.service;

import com.example.predict.auth.service.DauthProfile;
import com.example.predict.auth.service.DauthStudent;
import com.example.predict.user.domain.User;
import com.example.predict.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User upsertDauthStudent(DauthProfile profile) {
        DauthStudent student = profile.student();
        String studentId = buildStudentId(student);
        User user = userRepository.findByPublicId(profile.publicId())
                .or(() -> userRepository.findByStudentId(studentId))
                .orElseGet(() -> new User(
                        profile.publicId(),
                        studentId,
                        profile.username(),
                        profile.name(),
                        profile.phone(),
                        profile.profileImage(),
                        profile.status(),
                        student.grade(),
                        student.room(),
                        student.number()
                ));

        user.updateFromDauth(
                profile.publicId(),
                studentId,
                profile.username(),
                profile.name(),
                profile.phone(),
                profile.profileImage(),
                profile.status(),
                student.grade(),
                student.room(),
                student.number()
        );

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private String buildStudentId(DauthStudent student) {
        return "%d%02d%02d".formatted(student.grade(), student.room(), student.number());
    }
}
