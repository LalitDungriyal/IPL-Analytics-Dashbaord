import { React } from 'react';
import { Link } from 'react-router-dom';
import '../components/MatchSmallCard.scss';

export const MatchSmallCard = ({teamName,match}) => {

  const otherTeam = match?.team1===teamName?match?.team2:match?.team1;

  const isMatchWon=(teamName===match.matchWinner);

  let cardClassName='MatchDetailCard';

  
  if(isMatchWon) cardClassName+=' win-card';
  else cardClassName+=' lost-card';


  const otherTeamRoute=`/teams/${otherTeam}`;

  return (
    <div className={cardClassName}>
      <div className="heading-small-card">
       <div className="vs">vs</div>
       <div className="other-team-in-small-card"><h1><Link to={otherTeamRoute}>{otherTeam}</Link></h1></div>
       
        <p className="match-result">{match.matchWinner} won by {match.resultMargin} {match.result}</p>
        </div>
    </div>
  );
}
