package com.tks.gwa.controller;

import com.tks.gwa.entity.Proposal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposal")
public interface ProposalWS {
    @RequestMapping(value = "/createProposal", method = RequestMethod.POST)
    ResponseEntity<Proposal> createProposal(@RequestParam("eventTitle") String eventTitle,
                                            @RequestParam("description") String description,
                                            @RequestParam("location") String location,
                                            @RequestParam("content") String content,
                                            @RequestParam("username") String username);

    @RequestMapping(value = "/getProposalList", method = RequestMethod.POST)
    ResponseEntity<List<Proposal>> getProposalList();

    @RequestMapping(value = "/getProposalByID", method = RequestMethod.POST)
    ResponseEntity<Proposal> getProposalByID(@RequestBody int propid);

    @RequestMapping(value = "/getProposalByUserID", method = RequestMethod.POST)
    ResponseEntity<List<Proposal>> getProposalByUserID(@RequestParam int userid);

}
