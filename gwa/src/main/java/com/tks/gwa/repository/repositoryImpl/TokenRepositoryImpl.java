package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Token;
import com.tks.gwa.repository.TokenRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TokenRepositoryImpl extends GenericRepositoryImpl<Token, Integer> implements TokenRepository {

    public TokenRepositoryImpl() {
        super(Token.class);
    }

    @Override
    public List<Token> findTokenByAccountID(int accountID) {

        String sql = "SELECT t FROM " + Token.class.getName() + " AS t WHERE t.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return query.getResultList();
    }

    @Override
    public Token checkExistToken(String token, int accountID) {

        String sql = "SELECT t FROM " + Token.class.getName() + " AS t WHERE t.token =:token " +
                "AND t.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("token", token);
        query.setParameter("accountID", accountID);

        Token result = null;

        try {
            result = (Token) query.getSingleResult();
        } catch (NoResultException e) {
            return result;
        }

        return result;
    }
}
