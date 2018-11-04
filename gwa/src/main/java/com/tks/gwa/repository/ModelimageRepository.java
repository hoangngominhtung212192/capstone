package com.tks.gwa.repository;

import com.tks.gwa.entity.Modelimage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelimageRepository extends GenericRepository<Modelimage, Integer> {

    /**
     *
     * @param modelimage
     * @return
     */
    public Modelimage createNew(Modelimage modelimage);

    /**
     *
     * @param modelID
     * @return
     */
    public List<Modelimage> findImagesByModelID(int modelID);

    /**
     *
     * @param url
     * @return
     */
    public Modelimage findImagesByUrl(String url);
}
