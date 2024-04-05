package com.security30.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/country")
public class Country {
    @GetMapping
    public ResponseEntity<?> addCountry(){
        return new ResponseEntity<>("Done !!", HttpStatus.OK);
    }

    @GetMapping("/visit")
    public ResponseEntity<?> addCountryVisit(){
        return new ResponseEntity<>("visit Done !!", HttpStatus.OK);
    }
}

