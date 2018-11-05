package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Account;
import com.tks.gwa.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AccountRepositoryImpl extends GenericRepositoryImpl<Account, Integer> implements AccountRepository {

    public AccountRepositoryImpl() {
        super(Account.class);
    }

    @Override
    public Account checkLogin(String username, String password) {

        String sql = "SELECT a FROM " + Account.class.getName()+ " AS a WHERE a.username =:username AND a.password =:password";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("username", username);
        query.setParameter("password", password);

        Account account = null;

        try {
            account = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return account;
    }

    @Override
    public Account saveUser(Account account) {

        Account newAccount = this.create(account);

        return newAccount;
    }

    @Override
    public Account findUserByUsername(String username) {

        String sql = "SELECT a FROM " + Account.class.getName()+ " AS a WHERE a.username =:username";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("username", username);

        Account account = null;

        try {
            account = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return account;
    }

    @Override
    public List<Account> getAllAccount(int pageNumber, int pageSize) {

        String sql = "SELECT a FROM " + Account.class.getName() + " AS a ORDER BY a.createdDate DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Account> accountList = query.getResultList();

        return accountList;
    }

    @Override
    public int getCountAllAccount() {
        String sql = "SELECT count(a.id) FROM " + Account.class.getName() + " AS a";

        Query query = this.entityManager.createQuery(sql);

        return (int) (long) query.getSingleResult();
    }
}
