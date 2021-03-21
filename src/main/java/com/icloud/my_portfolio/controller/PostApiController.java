package com.icloud.my_portfolio.controller;


import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostQueryRepository postQueryRepository;

    @GetMapping("/api/posts")
    public Page<PostListDto> postList(Pageable pageable) {
        return postQueryRepository.findAllByStatus(PostStatus.Y, pageable);
    }
}
