import React from 'react'
import * as _ from 'lodash';
import { connect } from 'react-redux'

import { getBotDetails, submitEvent } from '../actions'
import PlayerBotDetails from '../components/PlayerBotDetails';

const mapStateToProps = (state, ownProps) => {
  return {
    botName: _.get(ownProps, "match.params.botName", ""),
    bot: _.get(state, "botDetails.item", []),
    lifecycleStatuses: _.get(state, "system.lifecycleStatuses", []),
    screenTests: _.get(state, "system.screenTests", [])
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onMountCallback: (botName) => (dispatch(getBotDetails(botName))),
    handleSubmit: (lifecycleStatus, screenTest, botName) => {
        let newEvent = _.set({}, "playerBot.name", botName);

        newEvent.processingLifecycleStatus = lifecycleStatus;
        newEvent.assumedScreenTest = screenTest;
        newEvent.timeRequested = new Date();

        dispatch(submitEvent(newEvent));
    }
  }
}

const PlayerBotDetailsContainer = connect(mapStateToProps, mapDispatchToProps)(PlayerBotDetails);

export default PlayerBotDetailsContainer


