package com.lucia.nft.controllers;

import java.util.List;

import com.lucia.nft.dto.UserSummaryDto;
import com.lucia.nft.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<UserSummaryDto>> searchUsers(@RequestParam(value = "term") String term) {
        return ResponseEntity.ok(userService.searchUsers(term));
    }
}
