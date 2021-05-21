package com.icloud.my_portfolio.account.dto;


import com.icloud.my_portfolio.comment.dto.CommentDto;
import com.icloud.my_portfolio.post.dto.PostDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean enabled;
    private LocalDateTime createdDate;

    private List<String> roleList = new ArrayList<>();

    private List<PostDto> postDtos;
    private List<CommentDto> commentDtos;
}
