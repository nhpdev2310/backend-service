package com.nhpdev.backendservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class RoleDetailResponse implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<String> permissions;
}
