package com.lucia.nft.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sold")
public class Sold {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceSoldGenerator")
    @GenericGenerator(
            name = "sequenceSoldGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",

            parameters = {
                    @Parameter(name = "sequence_name", value = "sold_sequence"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false)
    @Size(max = 100)
    private String price;

    @Column(nullable = false)  
    private String path;

    @Column(nullable = false)  
    private String hash;

    @Column(nullable = false)  
    private String c_like;

    @ManyToOne(fetch = FetchType.LAZY)          //No quiero descargar todo el user al descargar un sold, solo cuando se haga un get (FetchType.LAZY)
    @JoinColumn(name = "minter")       
    private User userMinter;

    @ManyToOne(fetch = FetchType.LAZY)          
    @JoinColumn(name = "buyer")
    private User userBuyer;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public Sold() {
        super();
    }

    public Sold(Long id, @Size(max = 100) String title, @Size(max = 100) String price, String path, String hash, String c_like, User userMinter, User userBuyer, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.path = path;
        this.hash = hash;
        this.c_like = c_like;
        this.userMinter = userMinter;
        this.userBuyer = userBuyer;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLike() {
        return this.c_like;
    }

    public void setLike(String c_like) {
        this.c_like = c_like;
    }

    public User getUserMinter() {
        return this.userMinter;
    }

    public void setUserMinter(User userMinter) {
        this.userMinter = userMinter;
    }

    public User getUserBuyer() {
        return this.userBuyer;
    }

    public void setUserBuyer(User userBuyer) {
        this.userBuyer = userBuyer;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

