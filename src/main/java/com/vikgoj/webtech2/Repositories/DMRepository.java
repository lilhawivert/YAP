package com.vikgoj.webtech2.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikgoj.webtech2.Entities.DM;




public interface DMRepository extends JpaRepository<DM, Long> {

    List<DM> findAllBySenderAndReceiver(String user1, String user2);

    List<DM> findAllBySender(String sender);
    List<DM> findAllByReceiver(String receiver);

}
