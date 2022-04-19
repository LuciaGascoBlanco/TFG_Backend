package com.lucia.nft.repositories;

import java.util.List;

import com.lucia.nft.entities.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserId(Long id);

    @Query("select msg from Message msg where msg.user.id in :ids order by msg.createdDate desc")
    List<Message> findMessages(@Param("ids") Long ids);
}
