package com.project.ipldashboard.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ipldashboard.model.Match;
import com.project.ipldashboard.model.Team;
import com.project.ipldashboard.repository.MatchRepository;
import com.project.ipldashboard.repository.TeamRepository;

@RestController
@CrossOrigin
public class TeamController {
    
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository,MatchRepository matchRepository)
    {
        this.matchRepository=matchRepository;
        this.teamRepository=teamRepository;
    }

    @GetMapping("/team")
    public Iterable<Team> getAllTeam()
    {
        return this.teamRepository.findAll();

    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName)
    {
        Team team = this.teamRepository.findByTeamName(teamName);

        team.setMatches(this.matchRepository.findLatestMatchesByTeam(teamName,4));

        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName,@RequestParam int year)
    {
        String start= String.valueOf(LocalDate.of(year, 1, 1));
        String end= String.valueOf(LocalDate.of(year+1, 1, 1));

        System.out.println(start+" "+end);

        return this.matchRepository.getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(teamName,start,end, teamName, start, end);


    }
}
