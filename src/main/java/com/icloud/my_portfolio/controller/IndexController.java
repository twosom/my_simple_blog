package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.repository.PostQueryRepository;
import com.icloud.my_portfolio.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Stream;


@Controller
@RequiredArgsConstructor
public class IndexController {

    //    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

//    @GetMapping("/")
//    public String home(Model model, Pageable pageable) {
//
////        Page<Post> all = postRepository.findAll(pageable);
//        Page<Post> all = postRepository.findAllByStatus(PostStatus.Y, pageable);
//
//
//        model.addAttribute("posts", all);
//        return "index";
//    }


    @GetMapping("/")
    public String home(Model model, Pageable pageable) {
        Page<PostListDto> all = postQueryRepository.findAllByStatus(PostStatus.Y, pageable);

        model.addAttribute("posts", all);
        return "index";
    }


    @GetMapping("/about")
    public String about() {
        return "about";
    }


}
