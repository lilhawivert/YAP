package com.vikgoj.webtech2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Yap;

public interface YapRepository extends JpaRepository<Yap, Long> {
    public boolean existsById(Long id);
    public Optional<Yap> findById(Long id);
}
