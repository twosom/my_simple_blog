package com.icloud.my_portfolio.role.service;

import com.icloud.my_portfolio.role.Role;
import com.icloud.my_portfolio.role.dto.RoleDto;
import com.icloud.my_portfolio.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleHierarchyImpl roleHierarchy;
    private final ModelMapper mapper;

    public List<Role> getRoles() {
        return roleRepository.findAllRolesByRoleGrade();
    }

    public void createRole(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        roleRepository.save(role);
    }

    public RoleDto getRole(Long id) {
        Role role = roleRepository.findById(id).orElse(new Role());
        return mapper.map(role, RoleDto.class);
    }

    public void removeRole(Long id) {
        roleRepository.deleteById(id);
    }

    public void reloadRoleHierarchy() {
        String roleHierarchyString = getRoleHierarchy();

        this.roleHierarchy.setHierarchy(roleHierarchyString);
    }

    public String getRoleHierarchy() {
        List<Role> allRoles = roleRepository.findAllRolesByRoleGrade();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allRoles.size(); i++) {
            try {
                /* Index 검증 */
                allRoles.get(i + 1);

                sb.append(allRoles.get(i).getRoleName());
                sb.append(" > ");
                sb.append(allRoles.get(i + 1).getRoleName());
                sb.append("\n");
            } catch (Exception e) {
                break;
            }
        }
        return sb.toString();
    }

}

