package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.UserWS;
import com.tks.gwa.dto.StatisticDTO;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Role;
import com.tks.gwa.entity.Token;
import com.tks.gwa.service.FileUploadService;
import com.tks.gwa.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
public class UserWsImpl implements UserWS {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public ResponseEntity<Account> checklogin(@RequestBody Account account, HttpSession session) {

        System.out.println("[UserWS] Begin checkLogin with username: " + account.getUsername() +
        " & password: " + account.getPassword());

        Account result = userService.checkLogin(account);

        if (result == null) {
            Account responseAccount = new Account();
            responseAccount.setMessage("Incorrect username or password!");

            return new ResponseEntity<Account>(responseAccount, HttpStatus.valueOf(400));
        }

        if (result.getStatus().equals("Banned")) {
            Account responseAccount = new Account();
            responseAccount.setMessage("Sorry! Your account has been banned!");

            return new ResponseEntity<Account>(responseAccount, HttpStatus.valueOf(400));
        }

        System.out.println("[UserWS] Username " + account.getUsername() + " login successfully !");

        // set session
        session.setAttribute("ROLE", result.getRole().getName());
        session.setAttribute("USERNAME", result.getUsername());

        System.out.println("Welcome " + result.getRole().getName() + " " + account.getUsername() + "!");

        return new ResponseEntity<Account>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> logout(HttpSession session) {

        System.out.println("");
        System.out.println("[UserWS] Begin logout function");

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
            System.out.println("");
            System.out.println("[UserWS] Begin getAccountFromSession at Web Service with data: ");
            System.out.println("[UserWs] Username in session: " + username);

            account = userService.getAccountByUsername(username);

            if (account != null) {
                account.setPassword(null);

//                String role = (String) session.getAttribute("ROLE");
//                System.out.println("[UserWs] Role in session: " + role);
//
//                Role roleEntity = new Role();
//                roleEntity.setName(role);
//
//                account.setRole(roleEntity);
                return new ResponseEntity<Account>(account, HttpStatus.OK);
            }
        }

        return new ResponseEntity<Account>(account, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<Account> register(@RequestBody Account account) {

        System.out.println("");
        System.out.println("[UserWS] Begin register() with data: ");
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

        newAccount = new Account();
        newAccount.setMessage("Create account failed");
        return new ResponseEntity<Account>(newAccount, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<Profile> getUserProfile(int accountID) {

        System.out.println("[UserWS] Begin getUserProfile with accountID: " + accountID);

        Profile profile = userService.getUserProfileByAccountID(accountID);

        if (profile == null) {
            return new ResponseEntity<Profile>(profile, HttpStatus.valueOf(400));
        }

        Account account = profile.getAccount();

        account.setPassword(null);
        profile.setAccount(account);

        return new ResponseEntity<Profile>(profile, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) {

        System.out.println("[UserWS] Begin updateProfile() at Web Service with data:");
        System.out.println(profile.toString());

        Account account = new Account();
        account.setId(profile.getAccountID());
        profile.setAccount(account);

        Profile updatedProfile = userService.updateProfile(profile);

        if (updatedProfile != null) {
            return new ResponseEntity<Profile>(updatedProfile, HttpStatus.OK);
        }

        Profile errorProfile = new Profile();
        errorProfile.setMessage("This email has existed");
        return new ResponseEntity<Profile>(errorProfile, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<Profile> updateProfileImage(MultipartFile photoBtn, int id) {

        String filename = fileUploadService.storeFile(photoBtn);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(filename).toUriString();

        Profile profile = userService.getUserProfileByAccountID(id);

        if (profile != null) {
            profile.setAvatar(fileDownloadUri);
            Profile newProfile = userService.updateProfile(profile);

            if (newProfile != null) {
                return new ResponseEntity<Profile>(newProfile, HttpStatus.OK);
            }
        }

        return new ResponseEntity<Profile>(profile, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<List<Object>> searchAccount(int pageNumber, String type, String txtSearch, String orderBy) {
        System.out.println("[UserWS] Begin searchAccount() with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("Type: " + type);
        System.out.println("TxtSearch: " + txtSearch);
        System.out.println("OrderBy: " + orderBy);

        List<Object> resultList = userService.searchAccount(pageNumber, type, txtSearch, orderBy);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Profile>> getTopRanking() {

        List<Profile> result = userService.getTopRanking();

        return new ResponseEntity<List<Profile>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getStatistic(int accountID) {

        System.out.println("[UserWS] Begin getStatistic with accountID: " + accountID);

        List<Object> result = userService.getStatisticByAccountID(accountID);

        return new ResponseEntity<List<Object>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getAllUserRatingByAccountID(int pageNumber, int accountID) {

        System.out.println("[UserWS] Begin getAllUserRatingByAccountID with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("AccountID: " + accountID);

        List<Object> result = userService.getAllUserRatingByAccountID(pageNumber, accountID);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatisticDTO> getStatisticMobile(int accountID) {

        System.out.println("[UserWS] Begin getMBStatistic with accountID: " + accountID);

        StatisticDTO result = userService.getMBStatisticByAccountID(accountID);

        return new ResponseEntity<StatisticDTO>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> banAccount(int accountID) {

        System.out.println("[UserWS] Begin banAccount with accountID: " + accountID);

        userService.banAccount(accountID);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> unbanAccount(int accountID) {
        System.out.println("[UserWS] Begin unbanAccount with accountID: " + accountID);

        userService.unbanAccount(accountID);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateAccountRole(int accountID, String roleName) {

        System.out.println("[UserWS] Begin updateAccountRole with accountID: " + accountID + " and Role: " + roleName);

        userService.updateAccountRole(accountID, roleName);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Token> addToken(int accountID, String token) {

        System.out.println("[UserWS] Begin addToken with accountID: " + accountID + " and token: " + token);

        Token result = userService.addToken(token, accountID);

        if (result != null) {
            System.out.println("Save token successfully with accountID: " + accountID);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        System.out.println("User " + accountID + " with Token existed!!");
        return new ResponseEntity<>(new Token(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> forgotPassword(String email) {

        System.out.println("[UserWS] Begin forgotPassword with email: " + email);

        if (email != null) {
            try {
                String result = userService.sendEmail(email);

                if (!result.equalsIgnoreCase("This email does not exist!")) {
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.valueOf(400));
                }

            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new ResponseEntity<>("Error occured", HttpStatus.valueOf(400));
    }

}
