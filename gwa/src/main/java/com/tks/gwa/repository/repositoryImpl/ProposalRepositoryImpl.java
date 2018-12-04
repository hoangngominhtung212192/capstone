package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
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
    public int getCountAllProposal() {
        String sql = "SELECT COUNT(e) FROM " + Proposal.class.getName() + " AS e";
        Query query = this.entityManager.createQuery(sql);
        long result = 0;
        try{
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }

        return (int) result;
    }


    @Override
    public List<Proposal> getAllProposal(int pageNum) {
        String sql = "FROM " + Proposal.class.getName() + "";
        Query query = this.entityManager.createQuery(sql);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Proposal> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no proposal found");
            return listres;
        }
        return listres;
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
    @Override
    public int getCountByAccountID(int accountID) {

        String sql = "SELECT count(p.id) FROM " + Proposal.class.getName() + " AS p WHERE p.account.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return (int) (long) query.getSingleResult();
    }
}
