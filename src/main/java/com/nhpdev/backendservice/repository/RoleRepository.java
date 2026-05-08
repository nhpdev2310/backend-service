package com.nhpdev.backendservice.repository;

import com.nhpdev.backendservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);
    Optional<Role> findRoleByName(String name);
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.roleHasPermissions rhp LEFT JOIN FETCH rhp.permission")
    List<Role> findAllRoleWithPermission();

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.roleHasPermissions rhp " +
            "LEFT JOIN FETCH rhp.permission WHERE r.name IN :roleNames")
    List<Role> findAllRoleWithPermissionByRoleName(@Param("roleNames") List<String> roleNames);

    @Query("SELECT r FROM Role r JOIN FETCH r.roleHasPermissions rhp JOIN FETCH rhp.permission WHERE rhp.permission.name = :permissionName")
    List<Role> findAllRoleWithPermissionByPermissionName(@Param("permissionName") String permissionName);
}
