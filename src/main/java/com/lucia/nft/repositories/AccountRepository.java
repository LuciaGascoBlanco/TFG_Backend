package com.lucia.nft.repositories;


import com.lucia.nft.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT a FROM Account a WHERE a.hash LIKE :hash")
    Account findAccount(@Param("hash") String hash);
}
