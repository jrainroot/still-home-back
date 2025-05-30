package jrainroot.still_home.controller;

import jakarta.servlet.http.HttpServletResponse;
import jrainroot.still_home.entity.Post;
import jrainroot.still_home.service.PostService;
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

    @GetMapping("")
    public List<Post> getPosts() {
        List<Post> posts = this.postService.getList();
        return posts;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id") Long id) {
        Post post = this.postService.findById(id);
        return post;
    }
}
