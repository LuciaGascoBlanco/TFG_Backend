package com.lucia.nft.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ImageDto {

    private Long id;
    private String title;
    private String price;
    private String path;
    private String hash;
    private String like;
    private UserSummaryDto userDtoMinter;
    private UserSummaryDto userDto;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdDate;

    public ImageDto() {
        super();
    }

    public ImageDto(Long id, String title, String price, String path, String hash, String like, UserSummaryDto userDtoMinter, UserSummaryDto userDto, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.path = path;
        this.hash = hash;
        this.like = like;
        this.userDtoMinter = userDtoMinter;
        this.userDto = userDto;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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
        return this.like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public UserSummaryDto getUserDtoMinter() {
        return this.userDtoMinter;
    }

    public void setUserDtoMinter(UserSummaryDto userDtoMinter) {
        this.userDtoMinter = userDtoMinter;
    }

    public UserSummaryDto getUserDto() {
        return this.userDto;
    }

    public void setUserDto(UserSummaryDto userDto) {
        this.userDto = userDto;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
