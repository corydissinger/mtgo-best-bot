import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, browserHistory } from 'react-router'
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import 'babel-polyfill';
import reducer from './reducers';

const store = createStore(reducer);

//End boilerplate
//Components

import Main from './components/Main';
import Bots from './components/Bots';
import Trades from './components/Trades';

ReactDOM.render(
  <Provider store={store}>
    <div style={{ height: '100%' }}>
        <Router history={browserHistory}>
            <Route path="/" exact component={Main}/>
            <Route path="/bots" component={Bots}/>
            <Route path="/trades" component={Trades}/>
        </Router>
     </div>
  </Provider>,
  document.getElementById('root')
);
