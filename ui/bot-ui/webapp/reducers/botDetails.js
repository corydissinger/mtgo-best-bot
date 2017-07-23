import {actionTypes} from '../actions';
import * as _ from 'lodash';

const INITIAL_STATE = {
  item: {}
}

const botDetails = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case actionTypes.SET_BOT_DETAILS:
      return _.assign({}, state, { item: action.botDetails });
  }

  return state;
}

export default botDetails
