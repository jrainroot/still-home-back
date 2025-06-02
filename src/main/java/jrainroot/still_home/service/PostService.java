package jrainroot.still_home.service;

import jakarta.transaction.Transactional;
import jrainroot.still_home.entity.Post;
import jrainroot.still_home.global.ResultData.ResultData;
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

    public Optional<Post> findById(Long id) {
       return this.postRepository.findById(id);
    }

    @Transactional
    public ResultData<Post> create(String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();
        this.postRepository.save(post);
        return ResultData.of(
                "S-3",
                "게시물이 생성되었습니다.",
                post
        );
    }

    public ResultData<Post> modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        this.postRepository.save(post);

        return ResultData.of(
                "S-4",
                "%d번 게시물이 수정되었습니다.".formatted(post.getId()),
                post
        );
    }

    public ResultData<Post> deleteById(Long id) {
        this.postRepository.deleteById(id);
        return ResultData.of(
                "S-4",
                "%d번 게시물이 삭제되었습니다.".formatted(id),
                null
        );
    }
}
