package com.lucia.nft.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sold")
public class Sold {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceImageGenerator")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Column(nullable = false)  
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Sold() {
        super();
    }

    public Sold(Long id, Image image, String hash, User user) {
        this.id = id;
        this.image = image;
        this.hash = hash;
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

