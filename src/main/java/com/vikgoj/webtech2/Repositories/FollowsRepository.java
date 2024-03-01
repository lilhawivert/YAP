package com.vikgoj.webtech2.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Follow;

import jakarta.transaction.Transactional;



public interface FollowsRepository extends JpaRepository<Follow, Long> {
    public boolean existsByUserWhosFollowedAndUserWhoFollows(String userWhoFollows, String userWhosFollowed);
    public Follow findByUserWhosFollowedAndUserWhoFollows(String userWhoFollows, String userWhosFollowed);
    @Transactional
    public void deleteByUserWhosFollowedAndUserWhoFollows(String userWhoFollows, String userWhosFollowed);
}
