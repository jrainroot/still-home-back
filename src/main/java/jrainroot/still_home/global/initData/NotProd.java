package jrainroot.still_home.global.initData;

import jrainroot.still_home.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(PostService postService) {
        return args -> {
          postService.create("제목 1", "내용 1");
          postService.create("제목 2", "내용 2");
          postService.create("제목 3", "내용 3");
          postService.create("제목 4", "내용 4");
          postService.create("제목 5", "내용 5");
        };
    }
}
