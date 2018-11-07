package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradereport;
import org.springframework.stereotype.Repository;

@Repository
public interface TradereportRepository extends GenericRepository<Tradereport, Integer> {
    /**
     * Execute update on database to add new report record
     * @param tradereport
     * @return new report
     */
    Tradereport addNewReport(Tradereport tradereport);

    /**
     * Execute query on database to get report has tradepostID and Email
     * @param tradepostId
     * @param email
     * @return report
     */
    Tradereport findReportByTradepostIdAndEmail(int tradepostId, String email);
}
