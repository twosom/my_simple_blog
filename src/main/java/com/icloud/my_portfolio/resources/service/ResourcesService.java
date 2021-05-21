package com.icloud.my_portfolio.resources.service;

import com.icloud.my_portfolio.resources.Resources;
import com.icloud.my_portfolio.resources.dto.ResourcesDto;
import com.icloud.my_portfolio.resources.repository.ResourcesRepository;
import com.icloud.my_portfolio.role.Role;
import com.icloud.my_portfolio.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;
    private final ResourcesQueryDslRepository resourcesQueryDslRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    public List<Resources> getResources() {
        return resourcesRepository.findAll();
    }

    public void createResources(ResourcesDto resourcesDto) {
        Resources resources = mapper.map(resourcesDto, Resources.class);
        if (resourcesDto.getRoleList() != null) {
            List<Role> roleList = resourcesDto.getRoleList()
                    .stream().map(roleRepository::findByRoleName)
                    .collect(Collectors.toList());

            resources.setRoleList(roleList);
        }
        resourcesRepository.save(resources);
    }

    public void removeResources(Long id) {
        resourcesRepository.deleteById(id);
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourcesMap() {
        List<Resources> allResources = resourcesQueryDslRepository.getAllResources();

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        allResources.forEach(resources -> {
            List<ConfigAttribute> configAttributeList = resources.getRoleList()
                    .stream().map(Role::getRoleName)
                    .map(SecurityConfig::new)
                    .collect(Collectors.toList());

            result.put(new AntPathRequestMatcher(resources.getResourceName(), resources.getHttpMethod().name()), configAttributeList);
        });

        return result;
    }

    public ResourcesDto getResources(Long id) {
        Resources resources = resourcesRepository.findById(id)
                .orElse(new Resources());

        ResourcesDto resourcesDto = mapper.map(resources, ResourcesDto.class);

        if (resources.getRoleList() != null) {
            List<String> roleList = resources.getRoleList()
                    .stream().map(Role::getRoleName)
                    .collect(Collectors.toList());
            resourcesDto.setRoleList(roleList);
        }

        return resourcesDto;
    }
}
