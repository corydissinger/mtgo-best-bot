import React from 'react'
import { connect } from 'react-redux'
import { getTrades } from '../actions'
import * as _ from 'lodash';

class TradesContainer extends React.Component {
  componentDidMount() {
    if(_.isEmpty(this.props.bots)) {
      this.props.getTrades();
    }
  }

  render() {
    return (
      <div>
        <form onSubmit={e => {
          e.preventDefault()
          if (!input.value.trim()) {
            return
          }
          dispatch(setTrades(input.value))
          input.value = ''
        }}>
          <input ref={node => {
            input = node
          }} />
          <button type="submit">
            Add Todo
          </button>
        </form>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    bots: _.get(state, "bots.list", [])
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    getTrades: () => (dispatch(getTrades()))
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(TradesContainer)


