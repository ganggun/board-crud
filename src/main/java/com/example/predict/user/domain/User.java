package com.example.predict.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Schema(description = "서비스 사용자 정보")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "서비스 내부 사용자 ID", example = "10")
    private Long id;

    @Column(unique = true)
    @Schema(description = "DAuth publicId", example = "user-public-id")
    private String publicId;

    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "학번. 학년 + 반 2자리 + 번호 2자리 형식입니다.", example = "30105")
    private String studentId;

    @Schema(description = "DAuth username", example = "gildong")
    @Column(unique = true, length = 30)
    private String username;

    @Column(length = 100)
    @JsonIgnore
    @Schema(hidden = true)
    private String passwordHash;

    @Column(nullable = false)
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "01012345678")
    private String phone;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "프로필 이미지 URL")
    private String profileImage;

    @Schema(description = "DAuth 계정 상태", example = "ACTIVE")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "서비스 권한. 관리자 API는 ADMIN만 호출할 수 있습니다.", example = "USER")
    private UserRole role = UserRole.USER;

    @Schema(description = "학년", example = "3")
    private Integer grade;
    @Schema(description = "반", example = "1")
    private Integer room;
    @Schema(description = "번호", example = "5")
    private Integer number;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected User() {
    }

    public User(String publicId, String studentId, String username, String name, String phone,
                String profileImage, String status, Integer grade, Integer room, Integer number) {
        this.publicId = publicId;
        this.studentId = studentId;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.profileImage = profileImage;
        this.status = status;
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

    public User(String username, String passwordHash, String studentId, String name,
                Integer grade, Integer room, Integer number) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.studentId = studentId;
        this.name = name;
        this.grade = grade;
        this.room = room;
        this.number = number;
        this.status = "ACTIVE";
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateFromDauth(String publicId, String studentId, String username, String name,
                                String phone, String profileImage, String status,
                                Integer grade, Integer room, Integer number) {
        this.publicId = publicId;
        this.studentId = studentId;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.profileImage = profileImage;
        this.status = status;
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getRoom() {
        return room;
    }

    public Integer getNumber() {
        return number;
    }
}
