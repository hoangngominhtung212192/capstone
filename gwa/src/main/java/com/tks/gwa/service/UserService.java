package com.tks.gwa.service;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public Account checkLogin(Account account);

    public Account register(Account account);

    public Profile getUserProfileByAccountID(int accountID);
}
