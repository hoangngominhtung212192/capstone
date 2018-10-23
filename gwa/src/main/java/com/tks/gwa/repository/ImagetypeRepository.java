package com.tks.gwa.repository;

import com.tks.gwa.entity.Imagetype;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagetypeRepository extends GenericRepository<Imagetype, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public Imagetype findByName(String name);
}
