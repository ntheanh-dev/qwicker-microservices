package com.nta.postservice.repository;

import com.nta.postservice.entity.Post;
import com.nta.postservice.enums.PostStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p WHERE p.userId = :userId")
    List<Post> findPostsByUserId(@Param("userId") String userId);

    Optional<Post> findPostByIdAndStatus(String postId, PostStatus status);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.status IN :statusList")
    List<Post> findPostsByUserIdAndStatus(
            @Param("userId") String userId, @Param("statusList") List<PostStatus> statusList);

    @Query("SELECT p.status FROM Post p WHERE p.id =: postId")
    PostStatus findPostStatusByPostId(@Param("postId") String postId);

    long countByStatus(PostStatus status);

    @Query("SELECT v.name, COUNT(p) FROM Post p JOIN p.vehicleType v GROUP BY v.name")
    List<Object[]> countOrdersByVehicle();
}
