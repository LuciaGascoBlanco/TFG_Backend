package com.lucia.nft.entities;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "c_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceUserGenerator")
    @GenericGenerator(
            name = "sequenceUserGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "c_user_sequence"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 100)
    private String login;

    @Column(nullable = false)
    @Size(max = 100)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")    //Un cambio en el usuario borra, actualiza, etc, los mensajes que dependen de él (CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userMinter")   
    private List<Sold> soldMinter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userBuyer")
    private List<Sold> soldBuyer;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public User() {
        super();
    }

    public User(Long id, @Size(max = 100) String firstName, @Size(max = 100) String lastName, @Size(max = 100) String login, @Size(max = 100) String password, List<Message> messages, List<Image> images, List<Sold> soldMinter, List<Sold> soldBuyer, LocalDateTime createdDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.messages = messages;
        this.images = images;
        this.soldMinter = soldMinter;
        this.soldBuyer = soldBuyer;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Sold> getSoldMinter() {
        return this.soldMinter;
    }

    public void setSoldMinter(List<Sold> soldMinter) {
        this.soldMinter = soldMinter;
    }

    public List<Sold> getSoldBuyer() {
        return this.soldBuyer;
    }

    public void setSoldBuyer(List<Sold> soldBuyer) {
        this.soldBuyer = soldBuyer;
    }
}
