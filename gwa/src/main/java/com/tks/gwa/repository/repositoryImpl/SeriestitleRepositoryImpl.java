package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Seriestitle;
import com.tks.gwa.repository.GenericRepository;
import com.tks.gwa.repository.SeriestitleRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class SeriestitleRepositoryImpl extends GenericRepositoryImpl<Seriestitle, Integer> implements SeriestitleRepository {

    public SeriestitleRepositoryImpl() {
        super(Seriestitle.class);
    }

    @Override
    public Seriestitle findByName(String name) {

        String sql = "SELECT s FROM " + Seriestitle.class.getName()+ " AS s WHERE s.name=:name";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", name);

        Seriestitle seriestitle= null;

        try {
            seriestitle = (Seriestitle) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return seriestitle;
    }

    @Override
    public Seriestitle createNew(Seriestitle seriestitle) {

        Seriestitle newSeriestitle = this.create(seriestitle);

        return newSeriestitle;
    }

    @Override
    public List<Seriestitle> getAllSeriestitle() {
        return this.getAll();
    }
}
