package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradereport;
import com.tks.gwa.repository.TradereportRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TradereportRepositoryImpl  extends GenericRepositoryImpl<Tradereport, Integer> implements TradereportRepository{

    public TradereportRepositoryImpl() {
        super(Tradereport.class);
    }
    @Override
    public Tradereport addNewReport(Tradereport tradereport) {
        return this.create(tradereport);
    }
}
