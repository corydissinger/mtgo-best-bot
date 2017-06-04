import React from 'react';
import {Link} from 'react-router';
import * as _ from 'lodash';

export default class PlayerBotDetails extends React.Component {
  componentDidMount() {
    if(_.isEmpty(this.props.bot)) {
        this.props.onMountCallback(this.props.params.botName);
    }
  }

  render() {
    return (
      <div>
        <h1>{this.props.bot.name}</h1>

        <h2>Collection</h2>
        {_.map(this.props.bot.botCards, (aCard) => {
            return <p>{aCard.quantity} {aCard.card.name} {aCard.price}</p>
        })}

        <h2>Cameras</h2>
        <ul>
        {_.map(this.props.bot.botCameras, (aBotCamera) => {
            return <li><Link to={"/botcamera/id/" + aBotCamera.id}>Taken {aBotCamera.timeTaken}</Link></li>
        })}
        </ul>
      </div>
    )
  }
}