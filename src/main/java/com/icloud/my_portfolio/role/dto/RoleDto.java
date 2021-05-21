package com.icloud.my_portfolio.role.dto;

import com.icloud.my_portfolio.role.RoleGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

    private Long id;
    private String roleName;
    private String roleDesc;
    private RoleGrade roleGrade;
}
