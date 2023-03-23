package com.project.ipldashboard.repository;

import org.springframework.data.repository.CrudRepository;

import com.project.ipldashboard.model.Team;

public interface TeamRepository extends CrudRepository<Team,Long>{
    
    Team findByTeamName(String teamName);

}
