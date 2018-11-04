package com.tks.gwa.service;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public Account checkLogin(Account account);

    public Account register(Account account);

    public Profile getUserProfileByAccountID(int accountID);

    public Account getAccountByUsername(String username);

    public Profile updateProfile(Profile profile);

    public List<Object> getAllAccount(int pageNumber, String type);
}
