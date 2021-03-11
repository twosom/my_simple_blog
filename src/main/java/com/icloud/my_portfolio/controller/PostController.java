package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.CommentDto;
import com.icloud.my_portfolio.controller.dto.PostDto;
import com.icloud.my_portfolio.domain.Category;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.service.CategoryService;
import com.icloud.my_portfolio.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public String findByPost(@PathVariable(name = "id") Long id, Model model) {
        try {
            //N상태는 삭제된 상태,
            Post post = postService.findByIdAndStatus(id, PostStatus.Y);
            model.addAttribute("postDto", new PostDto(post));
            model.addAttribute("commentDto", new CommentDto());
            return "post/post";

        } catch (PostNotFoundException e) {
            /* id에 해당하는 게시글 찾지 못했을 시 에러메시지 추가해주기 */
            model.addAttribute("errMsg", id + "번 게시글을 찾지 못했습니다.");
            return "/index";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        try {
            Post post = postService.findByIdAndStatus(id, PostStatus.Y);
            PostDto createPost = new PostDto(post);
            //==Entity를 직접 노출하지 않기 위해서 Dto 사용==//
            model.addAttribute("editPost", createPost);
            return "post/edit";
        } catch (PostNotFoundException e) {
            /* id에 해당하는 게시글 찾지 못했을 시 에러메시지 추가해주기 */
            model.addAttribute("errMsg", id + "번 게시글을 찾지 못했습니다.");
            return "/index";
        }
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("categories", categoryService.findAll());
        return "post/new";
    }

    //..../posts로 으로 오는 경우
    @PostMapping
    public String createPost(@ModelAttribute(name = "postDto") @Valid PostDto createPost, BindingResult bindingResult, Model model) {
        System.out.println("createPost = " + createPost);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "post/new";
        }
        //==영속성 컨텍스트에 저장하기 위한 엔티티로 변환==//
        Post post = createPost.toEntity();

        post.setCategory(new Category(createPost.getCategoryId()));

        Post newPost = postService.createPost(post);
        model.addAttribute("post", newPost);
        //==저장을 완료하면 게시글로 리다이렉트==//
        return "redirect:/posts/" + newPost.getId();
    }

    @PostMapping("/{id}/edit")
    public String modifyPost(@PathVariable Long id, @ModelAttribute("editPost") @Valid PostDto createPost, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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
        return "redirect:/#/";
    }
}
