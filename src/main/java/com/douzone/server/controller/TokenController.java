package com.douzone.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
	@GetMapping("/re_issuance")
	public void reIssuance() {
	}
}
