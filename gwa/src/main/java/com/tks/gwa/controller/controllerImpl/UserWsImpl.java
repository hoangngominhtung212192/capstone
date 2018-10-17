package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.UserWS;
import com.tks.gwa.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserWsImpl implements UserWS {

    @Override
    public ResponseEntity<String> checklogin(@RequestBody Account account) {
        System.out.println(account.getUsername());
        System.out.println(account.getPassword());

        return ResponseEntity.ok("Successful!");
    }
}
