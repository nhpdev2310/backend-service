package com.nhpdev.backendservice.repository;

import com.nhpdev.backendservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);
    List<Permission> findAllByNameIn(List<String> permissionNames);

    Permission findPermissionByName(String name);
}
