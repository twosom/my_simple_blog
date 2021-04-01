package com.icloud.my_portfolio.service.post;

import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.repository.PostCustomRepository;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostCustomRepository postCustomRepository;

    //== 게시글 단건 조회 ==//
    public PostViewDto findOne(Long id) {
        Post findPost = postCustomRepository.findOne(id);

        return new PostViewDto(findPost);
    }


    //== 게시글 단건 조회 최적화 버전 ==//
    public PostViewDto findOneOptimization(Long id) {
        return postCustomRepository.findOneOptimization(id);
    }



    //== 인덱스 페이지용 포스트 조회 ==//
    public Page<PostListDto> findPosts(Pageable pageable) {
        Page<Post> allPosts = postCustomRepository.findAllPosts(pageable);

        List<PostListDto> content = allPosts.getContent()
                .stream().map(PostListDto::new)
                .collect(Collectors.toList());


        long total = allPosts.getTotalElements();

        return new PageImpl(content, pageable, total);
    }

    public Post findByIdAndUsername(Long id, String username) {
        return postCustomRepository.findByIdAndUsername(id, username);
    }
}
