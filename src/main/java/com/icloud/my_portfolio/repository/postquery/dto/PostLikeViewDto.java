package com.icloud.my_portfolio.repository.postquery.dto;

import com.icloud.my_portfolio.domain.PostLike;
import lombok.Data;

import java.util.List;

@Data
public class PostLikeViewDto {

    private Long id;
    private Long postId;
    private String username;

    public PostLikeViewDto(Long id, Long postId, String username) {
        this.id = id;
        this.postId = postId;
        this.username = username;
    }
}
