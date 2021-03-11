package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.CategoryDto;
import com.icloud.my_portfolio.domain.Category;
import com.icloud.my_portfolio.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String categories(Pageable pageable, Model model) {
        model.addAttribute("categories", categoryService.findAll(pageable));
        return "category/list";
    }

//    @GetMapping("/new")
//    public String newCategory(@ModelAttribute CategoryDto categoryDto) {
//        return "category/new";
//    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "category/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        /* Entity 대신 DTO를 Attribute에 추가 */
        model.addAttribute("categoryDto", new CategoryDto(categoryService.findOne(id)));
        return "category/edit";
    }

    @PostMapping
    public String createCategory(@ModelAttribute @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "category/new";
        }
        categoryService.createCategory(categoryDto.toEntity());
        return "redirect:/categories";
    }

    @PostMapping("/{id}/edit")
    public String modifyCategory(@PathVariable Long id, @ModelAttribute @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("id = " + id);
            return "category/edit";
        }
        categoryService.updateCategory(categoryDto.toEntity());
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
