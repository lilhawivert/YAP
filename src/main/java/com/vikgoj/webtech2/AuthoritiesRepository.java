package com.vikgoj.webtech2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.Authority;

public interface AuthoritiesRepository extends JpaRepository<Authority, String> { 

}
