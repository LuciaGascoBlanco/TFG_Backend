package com.lucia.nft.repositories;

import java.util.List;

import com.lucia.nft.entities.Sold;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldRepository extends JpaRepository<Sold, Long> {

    @Query(value = "SELECT s FROM Sold s WHERE s.user.id IN :id ORDER BY s.createdDate DESC")
    List<Sold> findSold(@Param("id") Long id);

    @Query(value = "SELECT s FROM Sold s WHERE s.hash LIKE :hash")
    Sold findImage(@Param("hash") String hash);
}
