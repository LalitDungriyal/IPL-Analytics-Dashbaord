package com.project.ipldashboard.data;

import java.sql.Date;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.project.ipldashboard.model.Match;


public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

  private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

  @Override
  public Match process(final MatchInput matchInput) {


    final Match match = new Match();
    match.setCity(matchInput.getCity());

    match.setDate(matchInput.getDate());

    match.setPlayerOfMatch(matchInput.getPlayer_of_Match());
    match.setVanue(matchInput.getVenue());

    // Set Team 1 and Team 2 depending on the innings order
    String firstInningsTeam, secondInningsTeam;

    if ("bat".equals(matchInput.getTossDecision())) {
        firstInningsTeam = matchInput.getTossWinner();
        secondInningsTeam = matchInput.getTossWinner().equals(matchInput.getTeam1()) 
            ? matchInput.getTeam2() : matchInput.getTeam1();

    } else {
        secondInningsTeam = matchInput.getTossWinner();
        firstInningsTeam = matchInput.getTossWinner().equals(matchInput.getTeam1()) 
            ? matchInput.getTeam2() : matchInput.getTeam1();
    }
    match.setTeam1(firstInningsTeam);
    match.setTeam2(secondInningsTeam);

    match.setTossWinner(matchInput.getTossWinner());
    match.setTossDecision(matchInput.getTossDecision());
    match.setMatchWinner(matchInput.getWinner());
    match.setResult(matchInput.getResult());
    match.setResultMargin(matchInput.getResult_margin());
    match.setUmpire1(matchInput.getUmpire1());
    match.setUmpire2(matchInput.getUmpire2());
    
        return match;
  }

}