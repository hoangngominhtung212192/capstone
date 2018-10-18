package com.tks.gwa.service;

import com.tks.gwa.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public Account checkLogin(Account account);

    public Account register(Account account);
}
