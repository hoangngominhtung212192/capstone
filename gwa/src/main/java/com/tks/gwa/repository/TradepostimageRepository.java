package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradepostimage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradepostimageRepository extends GenericRepository<Tradepostimage, Integer> {
    List<Tradepostimage> getImageByTradepostId(int tradepostId);
    String getThumbnailImgUrlByTradepostId(int tradepostId);
    Tradepostimage addImage(Tradepostimage tradepostimage);
    boolean deleteAllImageByTradepostId(int tradepostId);
}
