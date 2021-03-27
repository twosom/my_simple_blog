package com.icloud.my_portfolio.controller;

import com.icloud.my_portfolio.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;


    @PostMapping("/like/{postId}/active")
    public String activeLike(@PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String userName = authentication.getName();
        postLikeService.activeLike(postId, userName);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/like/{postId}/inactive")
    public String inactiveLike(@PathVariable Long postId, Authentication authentication) {
        //==현재 로그인한 유저 이름 가져오기==//
        String userName = authentication.getName();
        postLikeService.inactiveLike(postId, userName);

        return "redirect:/posts/" + postId;
    }
}
