package com.icloud.my_portfolio.post.controller;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.repository.UserRepository;
import com.icloud.my_portfolio.category.Category;
import com.icloud.my_portfolio.comment.dto.CommentDto;
import com.icloud.my_portfolio.post.dto.PostDto;
import com.icloud.my_portfolio.post.dto.PostEditDto;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.post.Post;
import com.icloud.my_portfolio.post.dto.PostViewDto;
import com.icloud.my_portfolio.category.service.CategoryService;
import com.icloud.my_portfolio.post.service.PostQueryService;
import com.icloud.my_portfolio.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;

    private final CategoryService categoryService;
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public String findByPost(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {

            PostViewDto postViewDto = postQueryService.findOneOptimization(id);
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

        Long findUserId = userRepository.findIdByUsername(name);
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
        Account account = new Account(createPost.getUserId());
        post.addUserAndCategory(account, new Category(createPost.getCategoryId()));

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
            PostEditDto editPostDto = new PostEditDto(post);
            model.addAttribute("editPostDto", editPostDto);
            model.addAttribute("categories", categories);
            return "post/edit";
        } catch (PostNotFoundException e) {
            /* id에 해당하는 게시글 찾지 못했을 시 에러메시지 추가해주기 */
            model.addAttribute("errorMsg", "잘못된 접근입니다.");
            return "/index";
        }
    }

    @PostMapping("/{id}/edit")
    public String modifyPost(@PathVariable Long id,
                             @ModelAttribute("editPostDto") @Valid PostEditDto postEditDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("editPostDto", postEditDto);

            return "post/edit";
        }
        Post post = postEditDto.toEntity();
        post.setCategory(new Category(postEditDto.getCategoryId()));
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
