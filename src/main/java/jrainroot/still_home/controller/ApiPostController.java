package jrainroot.still_home.controller;

import jakarta.servlet.http.HttpServletResponse;
import jrainroot.still_home.entity.Post;
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
    @GetMapping("")
    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
//        posts.add(new Post((1L)));
//        posts.add(new Post((2L)));
//        posts.add(new Post((3L)));

        return posts;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id") Long id) {
        Post post = new Post();
        return post;
    }
}
