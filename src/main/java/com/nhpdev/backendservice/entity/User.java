package com.nhpdev.backendservice.entity;

import com.nhpdev.backendservice.common.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    @Column(length = 100)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.INACTIVE;
}
