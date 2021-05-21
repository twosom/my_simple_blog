package com.icloud.my_portfolio.comment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.account.repository.UserRepository;
import com.icloud.my_portfolio.comment.dto.CommentDto;
import com.icloud.my_portfolio.comment.Comment;
import com.icloud.my_portfolio.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createComment(HttpServletRequest request, Model model) throws IOException {

//        User user = new User(commentDto.getUserId());

        CommentDto commentDto = new ObjectMapper().readValue(request.getInputStream(), CommentDto.class);
        Account account = userRepository.findByUsername(commentDto.getUsername());
        Comment comment = commentDto.toEntity();
        account.addComment(comment);
        model.addAttribute("comment", commentService.createComment(comment));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Created");
    }

    @PostMapping("/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        //==해당 댓글 삭제 후 해당 게시물로 다시 리다이렉트==//
        return "redirect:/posts/" + postId;
    }

}
