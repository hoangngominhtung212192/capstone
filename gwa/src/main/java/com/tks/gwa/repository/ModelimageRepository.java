package com.tks.gwa.repository;

import com.tks.gwa.entity.Modelimage;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelimageRepository extends GenericRepository<Modelimage, Integer> {

    /**
     *
     * @param modelimage
     * @return
     */
    public Modelimage createNew(Modelimage modelimage);
}
