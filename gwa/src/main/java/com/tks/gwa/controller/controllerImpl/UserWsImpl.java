package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.UserWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
public class UserWsImpl implements UserWS {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> checklogin(@RequestBody Account account, HttpSession session) {

        boolean checkAdmin = false;

        Account result = userService.checkLogin(account);

        if (result == null) {
            return new ResponseEntity<>("Incorrect username of password!", HttpStatus.valueOf(400));
        }

        if (result.getStatus().equals("Banned")) {
            return new ResponseEntity<>("Sorry! Your account has been banned!", HttpStatus.valueOf(400));
        }

        System.out.println("Username " + account.getUsername() + " login successfully !");

        if (result.getRole().getName().equalsIgnoreCase("ADMIN")) {
            session.setAttribute("ADMIN", "TRUE");
            checkAdmin = true;
        }

        session.setAttribute("USERNAME", result.getUsername());

        if (checkAdmin) {
            System.out.println("Welcome admin " + account.getUsername());
            return new ResponseEntity<>("ADMIN", HttpStatus.OK);
        }

        return new ResponseEntity<>("MEMBER", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> logout(HttpSession session) {

        System.out.println("[UserWsImpl] Begin logout function");

        Enumeration<String> enumAttrbiute = session.getAttributeNames();

        while (enumAttrbiute.hasMoreElements()) {
            String attr = enumAttrbiute.nextElement();
            session.removeAttribute(attr);
            System.out.println("Removed attribute: " + attr);
        }

        return new ResponseEntity<String>("Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getUserNameFromSession(HttpSession session) {

        String username = (String) session.getAttribute("USERNAME");

        System.out.println("Username in session: " + username);

        if (username == null) {
            return new ResponseEntity<>(username, HttpStatus.valueOf(400));
        }

        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Account> register(@RequestBody Account account) {

        System.out.println("Username: " + account.getUsername());
        System.out.println("Password: " + account.getPassword());
        System.out.println("Firstname: " + account.getFirstname());
        System.out.println("Lastname: " + account.getLastname());
        System.out.println("Email: " + account.getEmail());

        Account newAccount = userService.register(account);

        if (!newAccount.getMessage().equalsIgnoreCase("success")) {
            return new ResponseEntity<Account>(newAccount, HttpStatus.valueOf(400));
        }

        if (newAccount != null) {
            return new ResponseEntity<Account>(newAccount, HttpStatus.OK);
        }

        newAccount.setMessage("Create account failed");
        return new ResponseEntity<Account>(newAccount, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<Profile> getUserProfile(int accountID) {

        Profile profile = userService.getUserProfileByAccountID(accountID);

        if (profile == null) {
            return new ResponseEntity<Profile>(profile, HttpStatus.valueOf(400));
        }

        return new ResponseEntity<Profile>(profile, HttpStatus.OK);
    }
}
