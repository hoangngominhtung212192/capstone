package com.tks.gwa.repository;

import com.tks.gwa.entity.Seriestitle;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriestitleRepository extends GenericRepository<Seriestitle, Integer> {

    /**
     *
     * @param name
     * @return
     */
    public Seriestitle findByName(String name);

    /**
     *
     * @param seriestitle
     * @return
     */
    public Seriestitle createNew(Seriestitle seriestitle);
}
