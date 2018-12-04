package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ProposalWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Proposal;
import com.tks.gwa.service.ProposalService;
import com.tks.gwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProposalWSImpl implements ProposalWS {
    @Autowired
    private ProposalService proposalService;

    @Autowired
    private UserService userService;
    @Override
    public ResponseEntity<Proposal> createProposal(@RequestParam("eventTitle") String eventTitle,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("location") String location,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("username") String username) {

        System.out.println("logged in username: "+username);
        Account acc = userService.getAccountByUsername(username);
        Proposal proposal = new Proposal();
        proposal.setAccount(acc);
        proposal.setContent(content);
        proposal.setDescription(description);
        proposal.setLocation(location);
        proposal.setEventTitle(eventTitle);
        proposal.setStatus("Pending");
        Proposal newProp = proposalService.createProposal(proposal);
        return new ResponseEntity<>(newProp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getProposalList(Integer pageNum) {
//        System.out.println("pagenum "+pageNum);
        List<Object> resultList = proposalService.getAllProposal(pageNum);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Proposal> getProposalByID(@RequestBody int propid) {
        Proposal result = proposalService.getProposalById(propid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Proposal>> getProposalByUserID(@RequestParam int userid) {
        List<Proposal> resultList = proposalService.getProposalByUser(userid);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
