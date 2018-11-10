package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.entity.Proposal;
import com.tks.gwa.repository.ProposalRepository;
import com.tks.gwa.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProposalServiceImpl implements ProposalService {
    @Autowired
    private ProposalRepository proposalRepository;

    @Override
    public Proposal createProposal(Proposal proposal) {
        return proposalRepository.createProposal(proposal);
    }

    @Override
    public List<Proposal> getAllProposal() {
        return proposalRepository.getAllProposal();
    }

    @Override
    public List<Proposal> getProposalByUser(int id) {
        return proposalRepository.getProposalByUser(id);
    }

    @Override
    public Proposal getProposalById(int id) {
        return proposalRepository.getProposalById(id);
    }
}
