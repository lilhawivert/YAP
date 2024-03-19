package com.vikgoj.webtech2.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Block;

import jakarta.transaction.Transactional;



public interface BlockRepository extends JpaRepository<Block, Long> {
    public boolean existsByUserWhosBlockedAndUserWhoBlocks(String userWhoBlocks, String userWhosBlocked);
    public Block findByUserWhosBlockedAndUserWhoBlocks(String userWhoBlocks, String userWhosBlocked);
    public List<Block> findAllByUserWhoFollows(String userWhoFollows);
    @Transactional
    public void deleteByUserWhosBlockedAndUserWhoBlocks(String userWhoBlocks, String userWhosBlocked);
}
