package com.tks.gwa.controller;

import com.tks.gwa.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public interface UserWS {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    ResponseEntity<String> checklogin(@RequestBody Account account, HttpSession session);

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    ResponseEntity<String> logout(HttpSession session);

    @RequestMapping(value = "/getUsername", method =  RequestMethod.GET)
    ResponseEntity<String> getUserNameFromSession(HttpSession session);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    ResponseEntity<Account> register(@RequestBody Account account);
}
