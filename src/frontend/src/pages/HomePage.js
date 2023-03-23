import { React,useEffect, useState } from 'react';
import { TeamTile } from '../components/TeamTile';
import '../pages/HomePage.scss';


export const HomePage = () => {

  const [teams,setTeams] = useState([]);
  useEffect(
    ()=>{
      const fetchTeams = async()=>{
        const response=await fetch(`${process.env.REACT_APP_API_ROOT_URL}/team`);

        const data=await response.json();

        setTeams(data);
      }

      fetchTeams();
    },[]
   );

  return (
    <div className="HomePage">
    <div className="header-section"><h1 className="app-name">IPL Dashboard</h1></div>
      
      <div className="team-grid">

        { teams.filter(team=>(team.teamName!=="Team1")).map(team=><TeamTile key={team.id} teamName={team.teamName} />)}

      </div>

    </div>
  );
}

