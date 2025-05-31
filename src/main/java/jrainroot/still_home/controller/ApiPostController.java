package jrainroot.still_home.controller;

import jakarta.servlet.http.HttpServletResponse;
import jrainroot.still_home.entity.Post;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ApiPostController {
    private final PostService postService;

    // DTO
    @AllArgsConstructor
    @Getter
    public static class PostsResponse {
        private final List<Post> posts;
    }

    @GetMapping("")
    public ResultData<PostsResponse> getPosts() {
        List<Post> posts = this.postService.getList();
        return ResultData.of("S-1", "success", new PostsResponse(posts));
    }


    // DTO
    @AllArgsConstructor
    public static class PostResponse {
        private final Post post;
    }


    @GetMapping("/{id}")
    public ResultData<PostResponse> getPost(@PathVariable("id") Long id) {
        return this.postService.findById(id).map(post -> ResultData.of(
                "S-1",
                "success",
                new PostResponse(post)
        )).orElseGet(() -> ResultData.of(
                "F-1",
                "%d 번 게시물은 존재하지 않습니다.".formatted(id),
                null
        ));
    }
}
