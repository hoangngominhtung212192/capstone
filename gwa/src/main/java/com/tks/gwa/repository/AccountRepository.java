package com.tks.gwa.repository;

import com.tks.gwa.entity.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends GenericRepository<Account, Integer> {

    public Account checkLogin(String username, String password);

    public Account saveUser(Account account);

    public Account findUserByUsername(String username);
}
