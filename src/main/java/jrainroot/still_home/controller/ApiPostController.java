package jrainroot.still_home.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jrainroot.still_home.entity.Post;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.service.PostService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Getter
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

    @Data
    public static class WriteRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class WriteResponse {
        private final Post post;
    }
    @PostMapping("")
    public ResultData<WriteResponse> write(@Valid @RequestBody WriteRequest writeRequest) {
//        System.out.println(writeRequest.getSubject());
//        System.out.println(writeRequest.getContent());
        ResultData<Post> writeResult = this.postService.create(null, writeRequest.getTitle(), writeRequest.getContent());
        if (writeResult.isFail()) return (ResultData) writeResult;

        return ResultData.of(
                writeResult.getResultCode(),
                writeResult.getMsg(),
                new WriteResponse(writeResult.getData())
        );
    }

    @Data
    public static class ModifyRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse {
        private final Post post;
    }

    @PatchMapping("/{id}")
    public ResultData modify(@Valid @RequestBody ModifyRequest modifyRequest, @PathVariable("id") Long id) {
        Optional<Post> optionalPost = this.postService.findById(id);

        if(optionalPost.isEmpty()) return ResultData.of(
                "F-1",
                "%d번 게시물은 존재하지 않습니다.".formatted(id),
                null
        );

        // 회원 권한 체크 canModify();

        ResultData<Post> modifyResult = this.postService.modify(optionalPost.get(), modifyRequest.getTitle(), modifyRequest.getContent());

        return ResultData.of(
                modifyResult.getResultCode(),
                modifyResult.getMsg(),
                new ModifyResponse(modifyResult.getData())
        );
    }


    @AllArgsConstructor
    @Getter
    public static class DeleteResponse {
        private final Post post;
    }

    @DeleteMapping("/{id}")
    public ResultData<DeleteResponse> delete(@PathVariable("id") Long id) {
        Optional<Post> optionalPost = this.postService.findById(id);
        if(optionalPost.isEmpty()) return ResultData.of(
                "F-1",
                "%d번 게시물은 존재하지 않습니다.".formatted(id),
                null
        );
        ResultData<Post> deleteResult = postService.deleteById(id);
        return ResultData.of(
                deleteResult.getResultCode(),
                deleteResult.getMsg(),
                new DeleteResponse(optionalPost.get())
        );
    }
}
