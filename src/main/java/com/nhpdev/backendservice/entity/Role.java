package com.nhpdev.backendservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleHasPermission> roleHasPermissions = new ArrayList<>();

    public void addPermission(Permission permission) {
        List<String> permissionName = this.getRoleHasPermissions().stream().map(RoleHasPermission::getPermission)
                .map(Permission::getName).toList();
        if(!permissionName.contains(permission.getName())) {
            RoleHasPermission roleHasPermission = RoleHasPermission.builder()
                    .role(this)
                    .permission(permission)
                    .build();
            this.roleHasPermissions.add(roleHasPermission);
        }
    }
}
