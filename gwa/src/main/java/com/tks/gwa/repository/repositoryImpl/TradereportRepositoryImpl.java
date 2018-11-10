package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradereport;
import com.tks.gwa.repository.TradereportRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

@Repository
public class TradereportRepositoryImpl  extends GenericRepositoryImpl<Tradereport, Integer> implements TradereportRepository{

    public TradereportRepositoryImpl() {
        super(Tradereport.class);
    }
    @Override
    public Tradereport addNewReport(Tradereport tradereport) {
        return this.create(tradereport);
    }

    @Override
    public Tradereport findReportByTradepostIdAndEmail(int tradepostId, String email) {
        String sql = "SELECT tr FROM " + Tradereport.class.getName() + " AS tr WHERE tr.tradepost.id =:tradepostID AND tr.email =:email";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", tradepostId);
        query.setParameter("email", email);
        Tradereport result = null;
        try {
            result = (Tradereport) query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } catch (NonUniqueResultException e){
            e.printStackTrace();
        }
        return result;
    }
}
