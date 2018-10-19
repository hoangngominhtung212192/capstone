package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Role;
import com.tks.gwa.repository.AccountRepository;
import com.tks.gwa.repository.ProfileRepository;
import com.tks.gwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Account checkLogin(Account account) {

        Account result = accountRepository.checkLogin(account.getUsername(), account.getPassword());

        return result;
    }

    @Override
    public Account register(Account account) {

        Account existedUsername = accountRepository.findUserByUsername(account.getUsername());

        if (existedUsername != null) {
            account.setMessage("This username has been existed !");
            System.out.println("Duplicate username");
            return account;
        }

        Profile existedEmail = profileRepository.findProfileByEmail(account.getEmail());

        if (existedEmail != null) {
            account.setMessage("This email has been existed !");
            System.out.println("Duplicate email");
            return account;
        }

        // set member
        Role role = new Role();
        role.setId(1);
        account.setRole(role);
        account.setStatus("Active");

        Account newAccount = accountRepository.saveUser(account);

        Profile profile = new Profile();

        Account tempAccount = new Account();
        tempAccount.setId(newAccount.getId());
        profile.setAccount(tempAccount);

        profile.setEmail(account.getEmail());
        profile.setFirstName(account.getFirstname());
        profile.setLastName(account.getLastname());
        profile.setNumberOfRaters(0);
        profile.setNumberOfStars(0);

        if (newAccount != null) {
            Profile newProfile = profileRepository.createNewProfile(profile);

            if (newProfile != null) {
                newAccount.setMessage("success");
                System.out.println("Register successfully !");
            }
        }

        return newAccount;
    }

    @Override
    public Profile getUserProfileByAccountID(int accountID) {

        Profile profile = profileRepository.findProfileByAccountID(accountID);

        return profile;
    }
}
