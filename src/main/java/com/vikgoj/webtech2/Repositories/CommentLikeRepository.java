package com.vikgoj.webtech2.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.CommentLike;

import jakarta.transaction.Transactional;



public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    public boolean existsByUserThatLikedAndCommentId(String userThatLiked, Long commentId);

    @Transactional
    public void deleteByUserThatLikedAndCommentId(String userThatLiked, Long commentId);
}
