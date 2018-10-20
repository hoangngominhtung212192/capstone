package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.UserWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Role;
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
    public ResponseEntity<Account> checklogin(@RequestBody Account account, HttpSession session) {

        Account result = userService.checkLogin(account);

        if (result == null) {
            Account responseAccount = new Account();
            responseAccount.setMessage("Incorrect username of password!");

            return new ResponseEntity<Account>(responseAccount, HttpStatus.valueOf(400));
        }

        if (result.getStatus().equals("Banned")) {
            Account responseAccount = new Account();
            responseAccount.setMessage("Sorry! Your account has been banned!");

            return new ResponseEntity<Account>(responseAccount, HttpStatus.valueOf(400));
        }

        System.out.println("Username " + account.getUsername() + " login successfully !");

        // set session
        session.setAttribute("ROLE", result.getRole().getName());
        session.setAttribute("USERNAME", result.getUsername());

        System.out.println("Welcome " + result.getRole().getName() + " " + account.getUsername() + "!");

        return new ResponseEntity<Account>(result, HttpStatus.OK);
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
    public ResponseEntity<Account> getAccountFromSession(HttpSession session) {

        Account account = null;

        String username = (String) session.getAttribute("USERNAME");

        if (username != null) {
            System.out.println("Username in session: " + username);

            account = userService.getAccountByUsername(username);

            if (account != null) {
                String role = (String) session.getAttribute("ROLE");
                System.out.println("Role in session: " + role);

                Role roleEntity = new Role();
                roleEntity.setName(role);

                account.setRole(roleEntity);
                return new ResponseEntity<Account>(account, HttpStatus.OK);
            }
        }

        return new ResponseEntity<Account>(account, HttpStatus.valueOf(400));
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
