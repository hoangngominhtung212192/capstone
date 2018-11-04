package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Model;
import com.tks.gwa.entity.Modelrating;
import com.tks.gwa.repository.ModelratingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ModelratingRepositoryImpl extends GenericRepositoryImpl<Modelrating, Integer> implements ModelratingRepository {

    public ModelratingRepositoryImpl() {
        super(Modelrating.class);
    }

    @Override
    public Modelrating checkExistRating(Model model, Account account) {

        String sql = "SELECT m FROM " + Modelrating.class.getName() + " AS m WHERE m.model =:model AND m.account =:account";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("model", model);
        query.setParameter("account", account);

        Modelrating modelrating = null;

        try {
            modelrating = (Modelrating) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return modelrating;
    }

    @Override
    public List<Modelrating> getListModelRating(int pageNumber, Model model) {

        String sql = "SELECT m FROM " + Modelrating.class.getName() + " AS m WHERE m.model =:model ORDER BY m.date DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("model", model);
        query.setFirstResult((pageNumber-1)*10);
        query.setMaxResults(10);

        List<Modelrating> modelratingList = query.getResultList();

        return modelratingList;
    }

    @Override
    public int getCountModelRatingByModelID(Model model) {

        String sql = "SELECT count(m.id) FROM " + Modelrating.class.getName() + " AS m WHERE m.model =:model";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("model", model);

        return (int) (long) query.getSingleResult();
    }
}
