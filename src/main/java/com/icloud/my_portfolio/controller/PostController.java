package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.CommentDto;
import com.icloud.my_portfolio.controller.dto.PostDto;
import com.icloud.my_portfolio.controller.dto.PostEditDto;
import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.UserJpaRepository;
import com.icloud.my_portfolio.repository.postquery.PostQueryRepository;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import com.icloud.my_portfolio.service.CategoryService;
import com.icloud.my_portfolio.service.post.PostQueryService;
import com.icloud.my_portfolio.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;

    private final CategoryService categoryService;
    private final UserJpaRepository userJpaRepository;


    @GetMapping("/{id}")
    public String findByPost(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostViewDto postViewDto = postQueryService.findOne(id);
            model.addAttribute("postViewDto", postViewDto);
            model.addAttribute("commentDto", new CommentDto());
            return "post/post";

        } catch (PostNotFoundException e) {
            /* id에 해당하는 게시글 찾지 못했을 시 에러메시지 추가해주기 */
            redirectAttributes.addFlashAttribute("result", id + "번에 해당하는 게시글을 찾지 못했습니다.");
            return "redirect:/";
        }
    }


    @GetMapping("/new")
    public String newPost(Model model, Authentication authentication) {
        String name = authentication.getName();

        Long findUserId = userJpaRepository.findIdByName(name);
        PostDto postDto = new PostDto();
        //==현재 로그인되어있는 유저의 ID값 가져와서 DTO에 할당.

        postDto.setUserId(findUserId);
        model.addAttribute("postDto", postDto);
        model.addAttribute("categories", categoryService.findAll());
        return "post/new";
    }

    //..../posts로 으로 오는 경우
    @PostMapping
    public String createPost(@ModelAttribute(name = "postDto") @Valid PostDto createPost,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "post/new";
        }
        //==영속성 컨텍스트에 저장하기 위한 엔티티로 변환==//
        Post post = createPost.toEntity();
        User user = new User(createPost.getUserId());
        post.addUserAndCategory(user, new Category(createPost.getCategoryId()));

        Post newPost = postService.createPost(post);
        /* 점검 */
        model.addAttribute("post", newPost);
        //==저장을 완료하면 게시글로 리다이렉트==//
        return "redirect:/posts/" + newPost.getId();
    }


    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model, Authentication authentication) {
        try {
            /* id 와 name 으로 post를 찾고 없으면 edit로 redirect */

            String username = authentication.getName();
            Post post = postQueryService.findByIdAndUsername(id, username);
            List<Category> categories = categoryService.findAll();
            PostEditDto editPostDto = new PostEditDto(post, categories);
            model.addAttribute("editPostDto", editPostDto);
            return "post/edit";
        } catch (PostNotFoundException e) {
            /* id에 해당하는 게시글 찾지 못했을 시 에러메시지 추가해주기 */
            model.addAttribute("errorMsg", "잘못된 접근입니다.");
            return "/index";
        }
    }

    @PostMapping("/{id}/edit")
    public String modifyPost(@PathVariable Long id,
                             @ModelAttribute("postDto") @Valid PostDto createPost,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "post/edit";
        }
        Post post = createPost.toEntity();
        post.setCategory(new Category(createPost.getCategoryId()));
        postService.updatePost(id, post);
        //==수정 완료 후 게시글로 리다이렉트==//
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }
}
