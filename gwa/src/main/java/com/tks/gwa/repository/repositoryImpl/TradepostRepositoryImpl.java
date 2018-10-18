package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradepost;
import com.tks.gwa.repository.TradepostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradepostRepositoryImpl extends GenericRepositoryImpl<Tradepost, Integer> implements TradepostRepository {

    public TradepostRepositoryImpl() {
        super(Tradepost.class);
    }

    @Override
    public List<Tradepost> getAllTradepost() {
        return this.getAll();
    }

    @Override
    public Tradepost addNewTradepost(Tradepost tradepost) {
        Tradepost newTradepost = this.create(tradepost);
        return newTradepost;
    }

    @Override
    public Tradepost editTradepost(Tradepost tradepost) {
        Tradepost updatedTradepost = this.update(tradepost);
        return updatedTradepost;
    }

    @Override
    public boolean removeTradepost(Tradepost tradepost) {
        this.delete(tradepost);
        return true;
    }
}
