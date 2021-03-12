package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.CommentDto;
import com.icloud.my_portfolio.controller.dto.PostDto;
import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.domain.User;
import com.icloud.my_portfolio.repository.UserRepository;
import com.icloud.my_portfolio.service.CommentService;
import com.icloud.my_portfolio.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public String createComment(@ModelAttribute @Valid CommentDto commentDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Post post = postService.findByIdAndStatus(commentDto.getPostId(), PostStatus.Y);
            model.addAttribute("postDto", new PostDto(post));
            return "post/post";
        }

        System.out.println("commentDto.getUserId() = " + commentDto.getUserId());
        User user = new User(commentDto.getUserId());
        Comment comment = commentDto.toEntity();
        user.addComment(comment);
        model.addAttribute("comment", commentService.createComment(comment));
        //==댓글 등록 완료하면 해당 게시물로 다시 리다이렉트==//
        return "redirect:/posts/" + commentDto.getPostId();
    }

    @PostMapping("/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        //==해당 댓글 삭제 후 해당 게시물로 다시 리다이렉트==//
        return "redirect:/posts/" + postId;
    }

}
