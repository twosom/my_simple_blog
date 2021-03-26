package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.repository.postquery.PostQueryRepository;
import com.icloud.my_portfolio.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {


    private final PostQueryRepository postQueryRepository;
    private final PostService postService;


    //    @GetMapping("/")
//    public String home(Model model, Pageable pageable) {
//        Page<PostListDto> all = postQueryRepository.findAllByStatus(PostStatus.Y, pageable);
//
//        model.addAttribute("posts", all);
//        return "index";
//    }
    @GetMapping("/")
    public String home(Model model, Pageable pageable) {
        Page<PostListDto> posts = postService.findPosts(pageable);

        model.addAttribute("posts", posts);
        return "index";
    }




    @GetMapping("/about")
    public String about() {
        return "about";
    }


}
