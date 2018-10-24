package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradepostimage;
import com.tks.gwa.repository.TradepostimageRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TradepostimageRepositoryImpl extends GenericRepositoryImpl<Tradepostimage, Integer>
        implements TradepostimageRepository {
    public TradepostimageRepositoryImpl() {
        super(Tradepostimage.class);
    }

    @Override
    public List<Tradepostimage> getImageByTradepostId(int tradepostId) {
        String sql = "SELECT img FROM " + Tradepostimage.class.getName() + " AS img WHERE img.tradepost.id =:tradepostID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", tradepostId);
        List<Tradepostimage> result = null;
        try{
            result = (List<Tradepostimage>) query.getResultList();
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getThumbnailImgUrlByTradepostId(int tradepostId) {
        String sql = "SELECT img FROM " + Tradepostimage.class.getName() + " AS img WHERE img.tradepost.id =:tradepostID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", tradepostId);
        query.setMaxResults(1);
         Tradepostimage result = null;
        try{
            result = (Tradepostimage)query.getSingleResult();
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return result.getImageUrl();
    }

    @Override
    public Tradepostimage addImage(Tradepostimage tradepostimage) {
        Tradepostimage result = this.create(tradepostimage);
        return result;
    }

    @Override
    public boolean deleteAllImageByTradepostId(int tradepostId) {
        String sql = "DELETE " + Tradepostimage.class.getName() + " AS img WHERE img.tradepost.id =:tradepostID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", tradepostId);
        return (query.executeUpdate() >= 0);
    }
}
