package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.CommentDto;
import com.icloud.my_portfolio.controller.dto.PostDto;
import com.icloud.my_portfolio.controller.dto.UserDto;
import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.UserRepository;
import com.icloud.my_portfolio.service.CategoryService;
import com.icloud.my_portfolio.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public String findByPost(@PathVariable(name = "id") Long id, Model model, Authentication authentication) {
        try {
            if (authentication != null) {
                String name = authentication.getName();
                User user = userRepository.findByName(name).get(0);
                UserDto userDto = new UserDto(user);
                System.out.println("userDto = " + userDto);
                model.addAttribute("userDto", userDto);
            }
            Post post = postService.findByIdAndStatus(id, PostStatus.Y);
            List<Comment> comments = post.getComments();

            List<Comment> enabledComments = new ArrayList<>();
            for (Comment comment : comments) {
                if (comment.getStatus() == CommentStatus.Y) {
                    enabledComments.add(comment);
                }
            }



            System.out.println("post = " + post);
//            Post post = postService.findByIdAndStatus(id, PostStatus.Y);
            model.addAttribute("postDto", new PostDto(post, enabledComments));
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
    public String newPost(Model model, Authentication authentication) {
        String name = authentication.getName();
        User findUser = userRepository.findByName(name).get(0);
        PostDto postDto = new PostDto();
        //==현재 로그인되어있는 유저의 ID값 가져와서 DTO에 할당.

        postDto.setUserId(findUser.getId());
        model.addAttribute("postDto", postDto);
        model.addAttribute("categories", categoryService.findAll());
        return "post/new";
    }

    //..../posts로 으로 오는 경우
    @PostMapping
    public String createPost(@ModelAttribute(name = "postDto") @Valid PostDto createPost, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "post/new";
        }
        //==영속성 컨텍스트에 저장하기 위한 엔티티로 변환==//
        Post post = createPost.toEntity();
        User user = new User(createPost.getUserId());
        post.setUser(user);

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
        return "redirect:/";
    }
}
