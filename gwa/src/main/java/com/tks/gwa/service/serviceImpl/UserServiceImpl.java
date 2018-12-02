package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.dto.StatisticDTO;
import com.tks.gwa.entity.*;
import com.tks.gwa.repository.*;
import com.tks.gwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TradepostRepository tradepostRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private TraderatingRepository traderatingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

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
    public List<Object> searchAccount(int pageNumber, String type, String txtSearch, String orderBy) {
        List<Object> result = new ArrayList<>();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCountResultAndLastpageAccount(AppConstant.PAGE_SIZE, txtSearch);
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

        List<Account> accountList = accountRepository.searchAccount(currentPage, AppConstant.PAGE_SIZE, txtSearch, orderBy);

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

    @Override
    public List<Profile> getTopRanking() {

        List<Profile> profileList = profileRepository.getTopRanking();

        int lastRating = 0;
        int rank = 0;

        if (profileList != null) {
            for (int i = 0; i < profileList.size(); i++) {
                Profile currentProfile = profileList.get(i);

                if (currentProfile.getNumberOfRaters() != 0) {
                    rank++;

                    int currentRating = Math.round((float) currentProfile.getNumberOfStars()
                            / (float) currentProfile.getNumberOfRaters());

                    if (currentRating == lastRating) {
                        rank--;
                        profileList.get(i).setRank(rank);
                    } else {
                        profileList.get(i).setRank(rank);
                    }

                    lastRating = currentRating;
                } else {
                    currentProfile.setRank(0);
                }
            }
        }

        return profileList;
    }

    @Override
    public List<Object> getStatisticByAccountID(int accountID) {

        List<Object> objectList = new ArrayList<>();

        // get sell records
        objectList.add(tradepostRepository.getCountTradepostByAccountIDAndTradetype(accountID, 1));

        // get buy records
        objectList.add(tradepostRepository.getCountTradepostByAccountIDAndTradetype(accountID, 2));

        // get count proposal
        objectList.add(proposalRepository.getCountByAccountID(accountID));

        return objectList;
    }

    @Override
    public List<Object> getAllUserRatingByAccountID(int pageNumber, int accountID) {

        List<Object> result = new ArrayList<>();

        int total = traderatingRepository.getCountByToUserID(accountID);

        if (total > 0) {
            int lastPage = 0;

            if (total % 10 == 0) {
                lastPage = total / 10;
            } else {
                lastPage = ((total / 10) + 1);
            }

            result.add(lastPage);

            List<Traderating> traderatingList = traderatingRepository.getListTradeRatingByToUserID(pageNumber, accountID);
            if (traderatingList != null) {
                for (int i = 0; i < traderatingList.size(); i++) {
                    Profile profile = profileRepository.findProfileByAccountID(traderatingList.get(i).getFromUser().getId());
                    traderatingList.get(i).getFromUser().setAvatar(profile.getAvatar());
                }

                result.add(traderatingList);

                return result;
            }
        }

        return null;
    }

    @Override
    public StatisticDTO getMBStatisticByAccountID(int accountID) {

        // get sell records
        int sell = tradepostRepository.getCountTradepostByAccountIDAndTradetype(accountID, 1);

        // get buy records
        int buy = tradepostRepository.getCountTradepostByAccountIDAndTradetype(accountID, 2);

        // get count proposal
        int proposal = proposalRepository.getCountByAccountID(accountID);

        StatisticDTO dto = new StatisticDTO(sell, buy, proposal);

        return dto;
    }

    @Override
    public void banAccount(int accountID) {
        Account account = accountRepository.read(accountID);

        if (account != null) {
            account.setStatus("Banned");

            accountRepository.update(account);
        }
    }

    @Override
    public void unbanAccount(int accountID) {
        Account account = accountRepository.read(accountID);

        if (account != null) {
            account.setStatus("Active");

            accountRepository.update(account);
        }
    }

    @Override
    public void updateAccountRole(int accountID, String roleName) {
        Account account = accountRepository.read(accountID);
        if(account!= null){
            Role role = roleRepository.getRoleByName(roleName);
            account.setRole(role);
            accountRepository.update(account);
        }
    }

    @Override
    public Token addToken(String token, int accountID) {

        Token result = null;

        Token exist_token = tokenRepository.checkExistToken(token, accountID);

        if (exist_token == null) {
            result = new Token();

            Account account = new Account();
            account.setId(accountID);
            result.setAccount(account);

            result.setToken(token);

            result = tokenRepository.create(result);
        }

        return result;
    }

    @Override
    public String sendEmail(String email) throws MessagingException, AddressException, IOException {

        Profile profile = profileRepository.findProfileByEmail(email);

        if (profile != null) {
            String fullName = "";

            if (profile.getMiddleName() != null) {
                fullName += profile.getLastName() + " " + profile.getMiddleName() + " " + profile.getFirstName();
            } else {
                fullName += profile.getLastName() + " " + profile.getMiddleName();
            }

            String content = "Hello <font color=\"darkblue\"><b>" + fullName + "</b></font>,<br/><br/>";

            content += "Thanks for join <font color=\"darkred\"><b>Gunpla World</b></font> !!<br/>";

            content += "Your password is: <b>" + profile.getAccount().getPassword() + "</b><br/><br/>";

            content += "Best regards,<br/>Gunpla World";

            return executeSendEmail(email, content);
        }

        return "This email does not exist!";
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    public int[] getCountResultAndLastpageAccount(int pageSize, String txtSearch) {

        int[] listResult = new int[2];
        int countResult = accountRepository.getCountSearchAccount(txtSearch);
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

    public String executeSendEmail(String email, String content) throws MessagingException, AddressException, IOException {

        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Gunpla World - Forgot password");
        msg.setSentDate(new Date());

        Multipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");

        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachPart = new MimeBodyPart();
        attachPart.attachFile("./image/gunpla_world.jpg");

        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);

        // send email
        Transport.send(msg);

        System.out.println("[UserService] Mail forgot-password has been sent successfully !!");

        return "We have sent your password to your email! Please check it!";
    }
}
