import React from 'react'
import * as _ from 'lodash';
import { connect } from 'react-redux'

import { getProcessingLifecycleStatuses, getAssumedScreenTests } from '../actions'
import Main from '../components/Main'

const mapDispatchToProps = (dispatch) => {
  return {
    onMountCallback: () => {
        dispatch(getProcessingLifecycleStatuses());
        dispatch(getAssumedScreenTests());
    }
  }
}

const MainContainer = connect(null, mapDispatchToProps)(Main);

export default MainContainer


