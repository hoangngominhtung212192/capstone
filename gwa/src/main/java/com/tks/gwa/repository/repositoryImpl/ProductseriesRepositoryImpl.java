package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Productseries;
import com.tks.gwa.repository.ProductseriesRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ProductseriesRepositoryImpl extends GenericRepositoryImpl<Productseries, Integer> implements ProductseriesRepository {

    public ProductseriesRepositoryImpl() {
        super(Productseries.class);
    }

    @Override
    public Productseries findByName(String name) {

        String sql = "SELECT p FROM " + Productseries.class.getName()+ " AS p WHERE p.name=:name";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", name);

        Productseries productseries = null;

        try {
            productseries = (Productseries) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return productseries;
    }

    @Override
    public Productseries createNew(Productseries productseries) {

        Productseries newProductseries = this.create(productseries);

        return newProductseries;
    }
}
