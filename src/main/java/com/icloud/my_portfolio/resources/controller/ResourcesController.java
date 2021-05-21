package com.icloud.my_portfolio.resources.controller;

import com.icloud.my_portfolio.resources.Resources;
import com.icloud.my_portfolio.resources.dto.ResourcesDto;
import com.icloud.my_portfolio.resources.service.ResourcesService;
import com.icloud.my_portfolio.role.service.RoleService;
import com.icloud.my_portfolio.security.metadata.UrlFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleService roleService;
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    @GetMapping("/admin/resources")
    public String allResources(Model model) {
        List<Resources> resourcesList = resourcesService.getResources();
        model.addAttribute("resourcesList", resourcesList);

        return "admin/resource/list";
    }

    @GetMapping("/admin/resources/register")
    public String createResourcesForm(Model model) {
        model.addAttribute("resources", new ResourcesDto());
        model.addAttribute("roleList", roleService.getRoles());

        return "admin/resource/detail";
    }

    @PostMapping("/admin/resources/register")
    public String createResources(ResourcesDto resourcesDto) {
        resourcesService.createResources(resourcesDto);
        urlFilterInvocationSecurityMetadataSource.reload();

        return "redirect:/admin/resources";
    }

    @GetMapping("/admin/resources/{id}")
    public String resourcesDetail(@PathVariable("id") Long id, Model model) {
        ResourcesDto resourcesDto = resourcesService.getResources(id);
        model.addAttribute("resources", resourcesDto);
        model.addAttribute("roleList", roleService.getRoles());

        return "admin/resource/detail";
    }

    @GetMapping("/admin/resources/delete/{id}")
    public String removeResources(@PathVariable("id") Long id) {
        resourcesService.removeResources(id);
        urlFilterInvocationSecurityMetadataSource.reload();

        return "redirect:/admin/resources";
    }
}
