package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Role;
import com.tks.gwa.repository.AccountRepository;
import com.tks.gwa.repository.ProfileRepository;
import com.tks.gwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        String createdDate = getCurrentTimeStamp();
        account.setCreatedDate(createdDate);

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

    @Override
    public Account getAccountByUsername(String username) {

        Account result = accountRepository.findUserByUsername(username);

        Profile profile = profileRepository.findProfileByAccountID(result.getId());

        if (profile != null) {
            result.setAvatar(profile.getAvatar());
        }

        return result;
    }

    @Override
    public Profile updateProfile(Profile profile) {

        Profile checkExistedEmail = profileRepository.findProfileByEmail(profile.getEmail());

        if (checkExistedEmail == null) {
            Profile newProfile = profileRepository.updateProfile(profile);

            return newProfile;
        } else {
            if (checkExistedEmail.getId() == profile.getId()) {
                Profile newProfile = profileRepository.updateProfile(profile);

                return newProfile;
            }
        }

        return null;
    }

    @Override
    public List<Object> getAllAccount(int pageNumber, String type) {
        List<Object> result = new ArrayList<>();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCountResultAndLastpageAccount(AppConstant.PAGE_SIZE);
        countTotal = (int) resultList[0];
        lastPage = (int) resultList[1];

        if (type.equals("First")) {
            currentPage = 1;
        } else if (type.equals("Prev")) {
            currentPage = pageNumber - 1;
        } else if (type.equals("Next")) {
            currentPage = pageNumber + 1;
        } else if (type.equals("Last")) {
            currentPage = lastPage;
        } else {
            currentPage = pageNumber;
        }

        if (currentPage <= 5) {
            beginPage = 1;
        } else if (currentPage % 5 != 0) {
            beginPage = ((int) (currentPage / 5)) * 5 + 1;
        } else {
            beginPage = ((currentPage / 5) - 1) * 5 + 1;
        }

        Pagination pagination = new Pagination(countTotal, currentPage, lastPage, beginPage);
        result.add(pagination);

        List<Account> accountList = accountRepository.getAllAccount(currentPage, AppConstant.PAGE_SIZE);

        if (accountList != null) {
            for (int i = 0; i < accountList.size(); i++) {
                Profile profile = profileRepository.findProfileByAccountID(accountList.get(i).getId());

                if (profile != null) {
                    accountList.get(i).setFirstname(profile.getFirstName());
                    accountList.get(i).setMiddlename(profile.getMiddleName());
                    accountList.get(i).setLastname(profile.getLastName());
                    accountList.get(i).setEmail(profile.getEmail());
                    accountList.get(i).setAddress(profile.getAddress());
                }
            }
        }

        if (accountList == null) {
            accountList = new ArrayList<Account>();
        }
        result.add(accountList);

        return result;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    public int[] getCountResultAndLastpageAccount(int pageSize) {

        int[] listResult = new int[2];
        int countResult = accountRepository.getCountAllAccount();
        listResult[0] = countResult;

        int lastPage = 1;

        if (countResult % pageSize == 0) {
            lastPage = countResult / pageSize;
        } else {
            lastPage = ((countResult / pageSize) + 1);
        }
        listResult[1] = lastPage;

        return listResult;
    }
}
