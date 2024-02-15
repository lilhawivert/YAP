package com.vikgoj.webtech2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Video;

public interface VideoRepository extends JpaRepository<Video, String> {
    public Video findByUrl(String url);
    public List<Video> findAllByUsername(String username);
}
