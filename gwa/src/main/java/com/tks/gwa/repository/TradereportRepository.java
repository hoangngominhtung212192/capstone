package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradereport;
import org.springframework.stereotype.Repository;

@Repository
public interface TradereportRepository extends GenericRepository<Tradereport, Integer> {
    Tradereport addNewReport(Tradereport tradereport);
}
