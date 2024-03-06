package com.vikgoj.webtech2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    public boolean existsByUsername(String username);
    public User findByUsername(String username);
    public void deleteByUsername(String username);
    public List<User> findAllByUsernameContaining(String username);
    public User getUserById(Long Id);
}
