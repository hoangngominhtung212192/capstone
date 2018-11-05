package com.tks.gwa.repository;

import com.tks.gwa.entity.Proposal;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends GenericRepository<Proposal, Integer> {

    /**
     *
     * @param accountID
     * @return
     */
    public int getCountByAccountID(int accountID);
}
