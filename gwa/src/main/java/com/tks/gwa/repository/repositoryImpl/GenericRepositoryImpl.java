package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Repository
public class GenericRepositoryImpl<T, ID extends Serializable> implements GenericRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     *
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected Class<T> entityClass;

    public GenericRepositoryImpl(){}

    /**
     *
     * @param entityClass
     */
    public GenericRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T t) {
        this.entityManager.persist(t);
        return t;
    }

    @Override
    public T read(ID id) {
        return this.entityManager.find(entityClass, id);
    }

    @Override
    public List<T> getAll() {
        List<T> list = this.entityManager.createQuery("from " + this.entityClass.getName()).getResultList();
        return list;
    }

    @Override
    public T update(T t) {
        return this.entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        this.entityManager.remove(t);
    }

    @Override
    public boolean exists(ID id) {
        T entity = this.entityManager.find(entityClass, id);

        return entity != null;
    }
}
