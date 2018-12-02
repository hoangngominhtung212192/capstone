package com.tks.gwa.repository;

import com.tks.gwa.entity.Token;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends GenericRepository<Token, Integer> {

    /**
     *
     * @param accountID
     * @return
     */
    public List<Token> findTokenByAccountID(int accountID);

    /**
     *
     * @param token
     * @return
     */
    public Token checkExistToken(String token, int accountID);
}
