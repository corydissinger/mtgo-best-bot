import React from 'react';
import {Link} from 'react-router-dom';
import * as _ from 'lodash';

export default class PlayerBots extends React.Component {
  componentDidMount() {
    if(_.isEmpty(this.props.bots)) {
      this.props.getBots();
    }
  }

  render() {
    return (
      <div>
        {_.map(this.props.bots, (aBot, index) => {
            return <Link key={index} to={"/bot/" + aBot.name}>{aBot.name}</Link>;
        })}
        {_.isEmpty(this.props.bots) &&
            <h1>You got an empty bot list, bruh</h1>
        }
      </div>
    )
  }
}