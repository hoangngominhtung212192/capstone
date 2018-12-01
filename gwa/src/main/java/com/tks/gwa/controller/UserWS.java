package com.tks.gwa.controller;

import com.tks.gwa.dto.StatisticDTO;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Token;
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

    @RequestMapping(value = "/searchAccount", method = RequestMethod.GET)
    ResponseEntity<List<Object>> searchAccount(@RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("type") String type,
                                               @RequestParam("txtSearch") String txtSearch,
                                               @RequestParam("orderBy") String orderBy);

    @RequestMapping(value = "/getTopRanking", method = RequestMethod.GET)
    ResponseEntity<List<Profile>> getTopRanking();

    @RequestMapping(value = "/getStatistic", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getStatistic(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/rating/getAll", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getAllUserRatingByAccountID(@RequestParam("pageNumber") int pageNumber,
                                                             @RequestParam("accountID") int accountID);

    @RequestMapping(value = "/getMBStatistic", method = RequestMethod.GET)
    ResponseEntity<StatisticDTO> getStatisticMobile(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    ResponseEntity<String> banAccount(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/unban", method = RequestMethod.POST)
    ResponseEntity<String> unbanAccount(@RequestParam("accountID") int accountID);

    @RequestMapping(value = "/update-role", method = RequestMethod.POST)
    ResponseEntity<String> updateAccountRole(@RequestParam("accountID") int accountID,
                                             @RequestParam("roleName") String roleName);

    @RequestMapping(value = "/addToken", method = RequestMethod.POST)
    ResponseEntity<Token> addToken(@RequestParam("accountID") int accountID,
                                   @RequestParam("token") String token);

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    ResponseEntity<String> forgotPassword(@RequestParam("email") String email);
}
