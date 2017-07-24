import * as _ from 'lodash';
import { actionTypes } from '../actions';

const INITIAL_STATE = {
  screenTests: [],
  lifecycleStatuses: [],
}

const system = (state = INITIAL_STATE, action) => {
  let newState = _.cloneDeep(state);

  switch (action.type) {
    case actionTypes.SET_SCREEN_TESTS:
        return _.assign({}, state, _.set(newState, "screenTests", action.enums));
    case actionTypes.SET_LIFECYCLE_STATUSES:
        return _.assign({}, state, _.set(newState, "lifecycleStatuses", action.enums));
  }

  return state;
}


export default system
