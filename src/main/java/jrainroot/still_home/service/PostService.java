package jrainroot.still_home.service;

import jrainroot.still_home.entity.Post;
import jrainroot.still_home.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> getList() {
        return this.postRepository.findAll();
    }

    public Post findById(Long id) {
        Optional<Post> optionalPost = this.postRepository.findById(id);
        if(optionalPost.isEmpty()) {
            return null;
        }

        return optionalPost.get();
    }

    public void create(String subject, String content) {
        Post post = Post.builder()
                .title(subject)
                .content(content)
                .build();
        this.postRepository.save(post);
    }
}
