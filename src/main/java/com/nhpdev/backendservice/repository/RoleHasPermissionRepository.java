package com.nhpdev.backendservice.repository;

import com.nhpdev.backendservice.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, String> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM RoleHasPermission rp WHERE rp.permission.id = :permissionId")
    int deleteByPermissionId(@Param("permissionId") String permissionId);
}
