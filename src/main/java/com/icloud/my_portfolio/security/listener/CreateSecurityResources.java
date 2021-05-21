package com.icloud.my_portfolio.security.listener;

import com.icloud.my_portfolio.account.repository.UserRepository;
import com.icloud.my_portfolio.resources.ResourceType;
import com.icloud.my_portfolio.resources.Resources;
import com.icloud.my_portfolio.resources.repository.ResourcesRepository;
import com.icloud.my_portfolio.role.Role;
import com.icloud.my_portfolio.role.RoleGrade;
import com.icloud.my_portfolio.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class CreateSecurityResources implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;

    public boolean alreadySetup = false;

    private static final AtomicInteger orderNum = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createSecurityResources();
    }

    private void createSecurityResources() {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자", RoleGrade.A);
        Role userRole = createRoleIfNotFound("ROLE_USER", "유저", RoleGrade.C);

        //                .antMatchers("/posts/new").hasRole("ROLE_ADMIN")
//                .antMatchers("/posts/{id}").permitAll()
//                .antMatchers("/posts/**").hasRole("ROLE_ADMIN")
//                .antMatchers("/categories/**").hasRole("ROLE_ADMIN")
//                .antMatchers(permitAllList).permitAll()

        createResourcesIfNotFound("/posts/new", ResourceType.URL, HttpMethod.GET, orderNum, Arrays.asList(adminRole));
        createResourcesIfNotFound("/posts/**", ResourceType.URL, HttpMethod.POST, orderNum, Arrays.asList(adminRole));
        createResourcesIfNotFound("/categories/**", ResourceType.URL, HttpMethod.GET, orderNum, Arrays.asList(adminRole));
        createResourcesIfNotFound("/categories", ResourceType.URL, HttpMethod.POST, orderNum, Arrays.asList(adminRole));
    }

    @Transactional
    public Resources createResourcesIfNotFound(String resourcesName,
                                               ResourceType resourceType,
                                               HttpMethod httpMethod,
                                               AtomicInteger orderNum, List<Role> roleList) {

        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourcesName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourcesName)
                    .resourceType(resourceType)
                    .httpMethod(httpMethod)
                    .orderNum(orderNum.incrementAndGet())
                    .roleList(roleList)
                    .build();

        }

        return resourcesRepository.save(resources);
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc, RoleGrade roleGrade) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .roleGrade(roleGrade)
                    .build();
        }

        return roleRepository.save(role);
    }
}
