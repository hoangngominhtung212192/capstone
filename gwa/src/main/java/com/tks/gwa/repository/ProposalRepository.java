package com.tks.gwa.repository;

import com.tks.gwa.entity.Proposal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends GenericRepository<Proposal, Integer>{
    Proposal createProposal(Proposal proposal);
    int getCountAllProposal();
    List<Proposal> getAllProposal(int pageNum);
    List<Proposal> getProposalByUser(int id);
    Proposal getProposalById(int id);


    /**
     *
     * @param accountID
     * @return
     */
    public int getCountByAccountID(int accountID);
}
