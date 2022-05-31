package com.douzone.server.controller;

import com.douzone.server.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class testController {

    private final TestService testService;
    private final Environment env;

    @GetMapping("/test")
    public ResponseEntity hello() {
        return new ResponseEntity(testService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/test2")
    public ResponseEntity hello2() {
        return new ResponseEntity("안녕하세요", HttpStatus.OK);
    }

    @GetMapping("/profile/server")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
                .findFirst().orElse("");
    }
}
