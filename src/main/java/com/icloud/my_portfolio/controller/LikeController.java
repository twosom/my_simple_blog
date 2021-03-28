package com.icloud.my_portfolio.controller;

import com.icloud.my_portfolio.service.CommentLikeService;
import com.icloud.my_portfolio.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;


    @PostMapping("/postlike/{postId}/active")
    public String activePostLike(@PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String username = authentication.getName();
        postLikeService.activeLike(postId, username);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/postlike/{postId}/inactive")
    public String inactivePostLike(@PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String username = authentication.getName();
        postLikeService.inactiveLike(postId, username);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/commentlike/{commentId}/active/{postId}")
    public String activeCommentLike(@PathVariable Long commentId, @PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String username = authentication.getName();
        commentLikeService.activeLike(commentId, username);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/commentlike/{commentId}/inactive/{postId}")
    public String inactiveCommentLike(@PathVariable Long commentId, @PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String username = authentication.getName();
        commentLikeService.inactiveLike(commentId, username);

        return "redirect:/posts/" + postId;
    }
}
