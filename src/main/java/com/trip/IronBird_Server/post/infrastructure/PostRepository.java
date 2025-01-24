package com.trip.IronBird_Server.post.infrastructure;

import com.trip.IronBird_Server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}
