package com.vikgoj.webtech2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Comment;
import com.vikgoj.webtech2.Entities.Yap;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByYap(Yap yap);
}
