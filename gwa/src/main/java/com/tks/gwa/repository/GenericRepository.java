package com.tks.gwa.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericRepository<T, ID> {

    /**
     * @param t
     * @return
     */
    T create(T t);

    /**
     * @param id
     * @return
     */
    T read(ID id);

    /**
     * @return
     */
    List<T> getAll();

    /**
     * @param t
     * @return
     */
    T update(T t);

    /**
     * @param t
     */
    void delete(T t);

    /**
     * @param id
     * @return
     */
    boolean exists(ID id);
}
