package com.lucia.nft.repositories;

import com.lucia.nft.entities.Sold;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldRepository extends JpaRepository<Sold, Long> {}
