package com.vikgoj.webtech2.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Like;

import jakarta.transaction.Transactional;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like, Long> {
    public boolean existsByUserThatLikedAndYapId(String userThatLiked, Long yapId);

    @Transactional
    public void deleteByUserThatLikedAndYapId(String userThatLiked, Long id);
}
