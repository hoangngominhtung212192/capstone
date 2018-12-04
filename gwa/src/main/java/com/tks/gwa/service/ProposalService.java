package com.tks.gwa.service;

import com.tks.gwa.entity.Proposal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProposalService {
    Proposal createProposal(Proposal proposal);
    List<Object> getAllProposal(Integer pageNum);
    List<Proposal> getProposalByUser(int id);
    Proposal getProposalById(int id);
}
