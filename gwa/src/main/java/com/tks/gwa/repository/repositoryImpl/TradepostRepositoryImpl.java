package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradepost;
import com.tks.gwa.repository.TradepostRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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
    public List<Tradepost> getAllTradepostByAccountID(int accountID) {
        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.account.id =:accountID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("accountID", accountID);
        List<Tradepost> result = null;
        try{
            result = (List<Tradepost>) query.getResultList();
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return result;
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

    @Override
    public Tradepost findTradepostById(int tradepostID) {
        return this.read(tradepostID);
    }

    @Override
    public int getCountAll() {

        String sql = "SELECT count(t.id) FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus='approved'";

        Query query = this.entityManager.createQuery(sql);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public List<Tradepost> get50TradePost(int pageNumber, int pageSize) {

        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus='approved' ORDER BY t.postedDate DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Tradepost> tradepostList = query.getResultList();

        return tradepostList;
    }
}
