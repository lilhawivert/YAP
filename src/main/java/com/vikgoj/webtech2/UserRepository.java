package com.vikgoj.webtech2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByUsername(String username);
    public User findByUsername(String username);
}
