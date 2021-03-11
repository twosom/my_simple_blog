package com.icloud.my_portfolio.controller;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @Test
    public void findByPost() throws Exception {
        given(this.postService.findByIdAndStatus(anyLong(), anyObject()))
                .willReturn(new Post("제목", "컨텐츠", "마크다운", PostStatus.Y));
        MvcResult mvcResult = this.mvc.perform(get("/posts/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();

        Post post = (Post) mvcResult.getModelAndView().getModel().get("post");
        Assertions.assertThat(post.getTitle()).isEqualTo("제목");
        Assertions.assertThat(post.getContent()).isEqualTo("컨텐츠");
        Assertions.assertThat(post.getCode()).isEqualTo("마크다운");
        Assertions.assertThat(post.getStatus()).isEqualTo(PostStatus.Y);
    }

//    @Test
//    public void newPost() throws Exception {
//        this.mvc.perform(get("/posts/new"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("post/new"))
//                .andReturn();
//    }
}