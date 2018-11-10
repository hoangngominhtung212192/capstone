package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Proposal;
import com.tks.gwa.repository.ProposalRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProposalRepositoryImpl extends GenericRepositoryImpl<Proposal, Integer> implements ProposalRepository {
    public ProposalRepositoryImpl() {super(Proposal.class);}

    @Override
    public Proposal createProposal(Proposal proposal) {
        System.out.println("user "+proposal.getAccount().getUsername()+" sending proposal");
        return this.create(proposal);
    }

    @Override
    public List<Proposal> getAllProposal() {
//        String sql = "FROM " + Proposal.class.getName() + " WHERE ";
        return this.getAll();
    }

    @Override
    public List<Proposal> getProposalByUser(int id) {
        String sql = "FROM " + Proposal.class.getName() + " WHERE accountID = :accountID";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", id);
        List<Proposal> result = null;
        try {
            result = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("no proposal found");
            return result;
        }
        return result;
    }

    @Override
    public Proposal getProposalById(int id) {
        return this.read(id);
    }
}
