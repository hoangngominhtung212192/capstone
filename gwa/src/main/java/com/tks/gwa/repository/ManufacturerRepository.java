package com.tks.gwa.repository;

import com.tks.gwa.entity.Manufacturer;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends GenericRepository<Manufacturer, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public Manufacturer findByName(String name);

    /**
     *
     * @param manufacturer
     * @return
     */
    public Manufacturer createNew(Manufacturer manufacturer);
}
