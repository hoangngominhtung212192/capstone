package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Modelimage;
import com.tks.gwa.repository.ModelimageRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ModelimageRepositoryImpl extends GenericRepositoryImpl<Modelimage, Integer> implements ModelimageRepository {

    public ModelimageRepositoryImpl() {
        super(Modelimage.class);
    }

    @Override
    public Modelimage createNew(Modelimage modelimage) {

        Modelimage newModelImage = this.create(modelimage);

        return newModelImage;
    }

    @Override
    public List<Modelimage> findImagesByModelID(int modelID) {

        String sql = "SELECT i FROM " + Modelimage.class.getName() + " AS i WHERE i.model.id =:modelID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("modelID", modelID);

        List<Modelimage> modelimageList = query.getResultList();

        return modelimageList;
    }

    @Override
    public Modelimage findImagesByUrl(String url) {
        String sql = "SELECT i FROM " + Modelimage.class.getName() + " AS i WHERE i.imageUrl =:url";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("url", url);

        Modelimage modelimage = null;

        try {
            modelimage = (Modelimage) query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        return modelimage;
    }
}
