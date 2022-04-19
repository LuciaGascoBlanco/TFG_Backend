package com.lucia.nft.controllers;

import java.io.IOException;
import java.util.List;

import com.lucia.nft.dto.ImageDto;
import com.lucia.nft.dto.MessageDto;
import com.lucia.nft.dto.UserDto;
import com.lucia.nft.services.CommunityService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    //MESSAGES

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@AuthenticationPrincipal UserDto user) {
        return ResponseEntity.ok(communityService.getMessages(user));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> postMessage(@AuthenticationPrincipal UserDto user, @RequestBody MessageDto messageDto) {
        return ResponseEntity.ok(communityService.postMessage(user, messageDto));
    }

    //IMAGES

    @GetMapping("/images")
    public ResponseEntity<List<ImageDto>> getImages(@AuthenticationPrincipal UserDto userDto) throws IOException {
        return ResponseEntity.ok(communityService.getImages(userDto));
    }

    @PostMapping("/images")
    public ResponseEntity<ImageDto> postImage(@AuthenticationPrincipal UserDto userDto, @RequestParam MultipartFile file, @RequestParam(value = "title") String title, @RequestParam(value = "price") String price) throws IOException {
        return ResponseEntity.ok(communityService.postImage(userDto, file, title, price));
    }

    //DELETE

    @DeleteMapping("/delete")
    public ResponseEntity<List<ImageDto>> deleteImages(@AuthenticationPrincipal UserDto userDto) throws IOException {
        return ResponseEntity.ok(communityService.deleteImage(userDto));
    }

    @DeleteMapping("/delete2")
    public ResponseEntity<List<ImageDto>> deleteImages2(@AuthenticationPrincipal UserDto userDto, @RequestParam(value = "hash") String hash) throws IOException {
        return ResponseEntity.ok(communityService.deleteImage2(userDto, hash));
    }

    //GALLERY

    @GetMapping("/gallery")
    public ResponseEntity<List<ImageDto>> getGallery(@AuthenticationPrincipal UserDto userDto) throws IOException {
        return ResponseEntity.ok(communityService.getGallery(userDto));
    }

    //SOLD

    @GetMapping("/imageSold")
    public ResponseEntity<List<ImageDto>> getSold(@AuthenticationPrincipal UserDto userDto) throws IOException {
        return ResponseEntity.ok(communityService.getSold(userDto));
    }

    @PostMapping("/imageSold")
    public ResponseEntity<List<ImageDto>> postSold(@AuthenticationPrincipal UserDto userDto, @RequestParam(value = "hash") String hash) throws IOException {
        return ResponseEntity.ok(communityService.postSold(userDto, hash));
    }

    //USER

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(userDto);
    }

}
