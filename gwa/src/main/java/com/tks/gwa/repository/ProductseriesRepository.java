package com.tks.gwa.repository;

import com.tks.gwa.entity.Productseries;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductseriesRepository extends GenericRepository<Productseries, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public Productseries findByName(String name);

    /**
     *
     * @param productseries
     * @return
     */
    public Productseries createNew(Productseries productseries);

    /**
     *
     * @return
     */
    public List<Productseries> getAllProductseries();
}
