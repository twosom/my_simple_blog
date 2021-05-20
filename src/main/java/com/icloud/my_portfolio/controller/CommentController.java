package com.icloud.my_portfolio.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.my_portfolio.controller.dto.CommentDto;
import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.User;
import com.icloud.my_portfolio.repository.PostCustomRepository;
import com.icloud.my_portfolio.repository.UserJpaRepository;
import com.icloud.my_portfolio.repository.postquery.dto.CommentViewDto;
import com.icloud.my_portfolio.service.CommentService;
import com.icloud.my_portfolio.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserJpaRepository userJpaRepository;
    private final PostService postService;

    private final PostCustomRepository postCustomRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createComment(HttpServletRequest request, Model model) throws IOException {

//        User user = new User(commentDto.getUserId());

        CommentDto commentDto = new ObjectMapper().readValue(request.getInputStream(), CommentDto.class);
        User user = userJpaRepository.findByName(commentDto.getUsername()).get(0);
        Comment comment = commentDto.toEntity();
        user.addComment(comment);
        model.addAttribute("comment", commentService.createComment(comment));
        List<CommentViewDto> commentAsDto = postCustomRepository.findCommentAsDto(commentDto.getPostId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Created");
//        return "redirect:/posts/" + commentDto.getPostId();
    }

    @PostMapping("/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        //==해당 댓글 삭제 후 해당 게시물로 다시 리다이렉트==//
        return "redirect:/posts/" + postId;
    }

}
