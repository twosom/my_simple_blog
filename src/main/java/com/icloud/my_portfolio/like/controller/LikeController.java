package com.icloud.my_portfolio.like.controller;

import com.icloud.my_portfolio.exception.CommentDeletedException;
import com.icloud.my_portfolio.exception.PostDeletedException;
import com.icloud.my_portfolio.like.comment.service.CommentLikeService;
import com.icloud.my_portfolio.like.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;


    @PostMapping("/postlike/{postId}")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, Object>> postLikeClickEvent(@PathVariable Long postId, Authentication authentication) {

        try {
            //==현재 로그인한 유저 이름 가져오기==//
            String username = authentication.getName();
            LinkedHashMap<String, Object> result = postLikeService.postLikeEvent(postId, username);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result);

        } catch (PostDeletedException e) {

            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            result.put("error", "해당 게시글이 삭제되었습니다.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }

    }

    @PostMapping("/commentlike/{commentId}")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, Object>> activeCommentLike(@PathVariable Long commentId, Authentication authentication) {
        try {
            //==현재 로그인한 유저 이름 가져오기==//
            String username = authentication.getName();
            LinkedHashMap<String, Object> result = commentLikeService.commentLikeEvent(commentId, username);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result);
        } catch (CommentDeletedException e) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            result.put("error", "해당 댓글이 삭제되었습니다.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }
    }
}
