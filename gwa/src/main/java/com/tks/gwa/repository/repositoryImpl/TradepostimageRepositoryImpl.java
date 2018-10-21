package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Tradepostimage;
import com.tks.gwa.repository.TradepostimageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradepostimageRepositoryImpl extends GenericRepositoryImpl<Tradepostimage, Integer>
        implements TradepostimageRepository {
    public TradepostimageRepositoryImpl(){ super(Tradepostimage.class);}
    @Override
    public List<Tradepostimage> getImageByTradepostId(int tradepostId) {
        return null;
    }

    @Override
    public Tradepostimage addImage(Tradepostimage tradepostimage) {
        Tradepostimage result = this.create(tradepostimage);
        return result;
    }
}
