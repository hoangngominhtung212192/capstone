package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Proposal;
import com.tks.gwa.repository.ProposalRepository;
import com.tks.gwa.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Object> getAllProposal(Integer pageNum) {
        List<Proposal> proposalList = proposalRepository.getAllProposal(pageNum);

        int totalRecord = proposalRepository.getCountAllProposal();
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(totalPage);
        result.add(proposalList);

        return result;
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
