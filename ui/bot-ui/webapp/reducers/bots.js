import * as _ from 'lodash';

const INITIAL_STATE = {
  list: []
}

const bots = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case 'SET_BOTS':
      return _.assign({}, state, { list: action.bots });
  }

  return state;
}


export default bots
