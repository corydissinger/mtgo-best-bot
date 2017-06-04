import React from 'react'
import * as _ from 'lodash';
import { connect } from 'react-redux'

import { getBotDetails } from '../actions'
import PlayerBotDetails from '../components/PlayerBotDetails';

const mapStateToProps = (state) => {
  return {
    bot: _.get(state, "botDetails.item", [])
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onMountCallback: (botName) => (dispatch(getBotDetails(botName)))
  }
}

const PlayerBotDetailsContainer = connect(mapStateToProps, mapDispatchToProps)(PlayerBotDetails);

export default PlayerBotDetailsContainer


