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
    public List<Account> searchAccount(int pageNumber, int pageSize, String txtValue, String orderBy) {

        boolean txtSearch_flag = false;

        String sql = "SELECT a FROM " + Account.class.getName() + " AS a";

        if (txtValue.length() != 0) {
            sql += " WHERE a.username LIKE :username";
            txtSearch_flag = true;
        }

        if (orderBy.equalsIgnoreCase("Ascending")) {
            sql += " ORDER BY a.createdDate ASC";
        } else {
            sql += " ORDER BY a.createdDate DESC";
        }

        Query query = this.entityManager.createQuery(sql);

        if (txtSearch_flag) {
            query.setParameter("username", "%" + txtValue + "%");
        }

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Account> accountList = query.getResultList();

        return accountList;
    }

    @Override
    public int getCountSearchAccount(String txtValue) {
        boolean txtSearch_flag = false;

        String sql = "SELECT count(a.id) FROM " + Account.class.getName() + " AS a";

        if (txtValue.length() != 0) {
            txtSearch_flag = true;
            sql += " WHERE a.username LIKE :username";
        }

        Query query = this.entityManager.createQuery(sql);

        if (txtSearch_flag) {
            query.setParameter("username", "%" + txtValue + "%");
        }

        return (int) (long) query.getSingleResult();
    }
}
