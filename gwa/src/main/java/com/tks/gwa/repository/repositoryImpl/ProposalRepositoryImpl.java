package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Proposal;
import com.tks.gwa.repository.GenericRepository;
import com.tks.gwa.repository.ProposalRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class ProposalRepositoryImpl extends GenericRepositoryImpl<Proposal, Integer> implements ProposalRepository {

    public ProposalRepositoryImpl() {
        super(Proposal.class);
    }

    @Override
    public int getCountByAccountID(int accountID) {

        String sql = "SELECT count(p.id) FROM " + Proposal.class.getName() + " AS p WHERE p.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return (int) (long) query.getSingleResult();
    }
}
