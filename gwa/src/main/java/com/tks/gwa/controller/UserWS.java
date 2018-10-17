package com.tks.gwa.controller;

import com.tks.gwa.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public interface UserWS {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    ResponseEntity<String> checklogin(@RequestBody Account account);
}
