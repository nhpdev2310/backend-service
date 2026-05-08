package com.nhpdev.backendservice.entity;

import com.nhpdev.backendservice.common.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserHasRole> userHasRoles = new ArrayList<>();

    public void addRole(Role role) {
        List<String> roleNames = this.userHasRoles.stream().map(UserHasRole::getRole)
                .map(Role::getName).toList();
        if(!roleNames.contains(role.getName())) {
            UserHasRole userHasRole = UserHasRole.builder()
                    .user(this)
                    .role(role)
                    .build();
            this.userHasRoles.add(userHasRole);
        }
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = this.userHasRoles.stream().map(UserHasRole::getRole).toList();
        List<String> roleNames = roles.stream().map(Role::getName).toList();
        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(UserStatus.ACTIVE);
    }
}
