package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(Model model, Pageable pageable) {

        Page<Post> all = postRepository.findAll(pageable);

        model.addAttribute("posts", all);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
