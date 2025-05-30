package jrainroot.still_home.repository;

import jrainroot.still_home.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
