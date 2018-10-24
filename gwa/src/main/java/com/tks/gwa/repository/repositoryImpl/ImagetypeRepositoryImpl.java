package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Imagetype;
import com.tks.gwa.repository.ImagetypeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ImagetypeRepositoryImpl extends GenericRepositoryImpl<Imagetype, Integer> implements ImagetypeRepository {

    public ImagetypeRepositoryImpl() {
        super(Imagetype.class);
    }

    @Override
    public Imagetype findByName(String name) {

        String sql = "SELECT i FROM " + Imagetype.class.getName()+ " AS i WHERE i.name =:name";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", name);

        Imagetype imagetype = null;

        try {
            imagetype = (Imagetype) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return imagetype;
    }
}
