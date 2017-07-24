import React from 'react'
import * as _ from 'lodash';
import { connect } from 'react-redux'

import { getBots } from '../actions'
import PlayerBots from '../components/PlayerBots';


const mapStateToProps = (state) => {
  return {
    bots: _.get(state, "bots.list", [])
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    getBots: () => (dispatch(getBots()))
  }
}

const BotsContainer = connect(mapStateToProps, mapDispatchToProps)(PlayerBots);

export default BotsContainer


