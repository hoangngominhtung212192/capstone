package com.tks.gwa.repository;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Model;
import com.tks.gwa.entity.Modelrating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelratingRepository extends GenericRepository<Modelrating, Integer> {

    /**
     *
     * @param model
     * @param account
     * @return
     */
    public Modelrating checkExistRating(Model model, Account account);

    /**
     *
     * @param pageNumber
     * @return
     */
    public List<Modelrating> getListModelRating(int pageNumber, Model model);

    /**
     *
     * @param model
     * @return
     */
    public int getCountModelRatingByModelID(Model model);
}
