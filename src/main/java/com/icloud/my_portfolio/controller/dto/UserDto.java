package com.icloud.my_portfolio.controller.dto;


import com.icloud.my_portfolio.domain.Role;
import com.icloud.my_portfolio.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean enabled;
    private Role role;
    private LocalDateTime createdDate;

    private List<PostDto> postDtos;
    private List<CommentDto> commentDtos;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public User toEntity() {
        return User.builder()
                .username(getUsername())
                .password(getPassword())
                .email(getEmail())
                .build();
    }
}
