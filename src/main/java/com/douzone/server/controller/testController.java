package com.douzone.server.controller;

import com.douzone.server.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testController {

    private final TestService testService;


    @GetMapping("/test")
    public ResponseEntity hello() {
        return new ResponseEntity(testService.findAll(), HttpStatus.OK);
    }

}
