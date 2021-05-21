package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.post.dto.PostListDto;
import com.icloud.my_portfolio.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostQueryService postQueryService;

    @GetMapping("/")
    public String home(Model model, Pageable pageable) {
        Page<PostListDto> posts = postQueryService.findPosts(pageable);

        model.addAttribute("posts", posts);
        return "index";
    }


    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/denied")
    public String accessDenied() {
        return "error/denied";
    }
}
