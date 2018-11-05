package com.tks.gwa.controller;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public interface UserWS {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    ResponseEntity<Account> checklogin(@RequestBody Account account, HttpSession session);

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    ResponseEntity<String> logout(HttpSession session);

    @RequestMapping(value = "/checkLogin", method =  RequestMethod.GET)
    ResponseEntity<Account> getAccountFromSession(HttpSession session);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    ResponseEntity<Account> register(@RequestBody Account account);

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    ResponseEntity<Profile> getUserProfile(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    ResponseEntity<Profile> updateProfile(@RequestBody Profile profile);

    @RequestMapping(value = "/profile/update/image", method = RequestMethod.POST)
    ResponseEntity<Profile> updateProfileImage(@RequestParam(value = "photoBtn", required = false) MultipartFile photoBtn,
                                               @RequestParam("id") int id);

    @RequestMapping(value = "/getAllAccount", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getAllAccount(@RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("type") String type);

    @RequestMapping(value = "/getTopRanking", method = RequestMethod.GET)
    ResponseEntity<List<Profile>> getTopRanking();

    @RequestMapping(value = "/getStatistic", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getStatistic(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/rating/getAll", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getAllUserRatingByAccountID(@RequestParam("pageNumber") int pageNumber,
                                                             @RequestParam("accountID") int accountID);
}
