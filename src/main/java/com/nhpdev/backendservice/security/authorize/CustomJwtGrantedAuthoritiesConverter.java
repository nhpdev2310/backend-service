package com.nhpdev.backendservice.security.authorize;

import com.nhpdev.backendservice.dto.response.RoleDetailResponse;
import com.nhpdev.backendservice.service.JwtService;
import com.nhpdev.backendservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final RoleService roleService;
    private final JwtService jwtService;
    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt source) {
        //Extract the authorities from jwt claim key = authorities (Roles)
        List<String> authorities = jwtService.extractAuthorities(source.getClaim("authorities"));
        //from role names get Role with its permission as
        List<RoleDetailResponse> roleWithPermission = roleService.getAllRoleWithPermissionByRoleName(authorities);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //Flat those role objects then
        //Add ROLEs to List of SimpleGrantedAuthority(STRING)
        //Then must add all the permission to List of SimpleGrantedAuthority(STRING)
        roleWithPermission.forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                role.getPermissions().forEach(permission ->
                        grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
            }
        });
        //Return List of SimpleGrantedAuthority(STRING)
        return grantedAuthorities;
    }
}
