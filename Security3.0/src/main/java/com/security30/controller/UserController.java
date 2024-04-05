package com.security30.controller;

import com.security30.model.UserProperty;
import com.security30.payload.JwtResponse;
import com.security30.payload.LoginDto;
import com.security30.payload.UserPropertyDto;
import com.security30.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserPropertyDto dto) {
        UserPropertyDto added = userService.addUserProperty(dto);
        if (added != null) {
            return new ResponseEntity<>("signup successfully Done !!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("signup failed", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        String jwt = userService.verifyLogin(loginDto);
        if(jwt!=null) {
            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credential !!", HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/profile")
    public UserProperty getProfile(@AuthenticationPrincipal UserProperty userProperty){
        return userProperty;
    }

    @GetMapping("/OAuth/access")
    public ResponseEntity<?> getAccessToken(@AuthenticationPrincipal OAuth2User principal) {
        // Check if the user is authenticated
        if (principal != null && principal.getName() != null) {
            // User is authenticated, you can access their details
            String jwt = userService.getOauth2Access(principal);
            if (jwt != null) {
                JwtResponse response = new JwtResponse();
                response.setToken(jwt);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
            // User is not authenticated, redirect to login page or handle accordingly
        return new ResponseEntity<>("Something went wrong !! ", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
