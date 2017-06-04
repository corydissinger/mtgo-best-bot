import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, browserHistory } from 'react-router'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import { createLogger } from 'redux-logger'
import thunkMiddleware from 'redux-thunk'
import reducer from './reducers';

const store = createStore(reducer, applyMiddleware(
    createLogger({ level: 'info', diff: true}),
    thunkMiddleware
));

//End boilerplate
//Components

import Main from './components/Main';
import BotsContainer from './containers/BotsContainer';
import TradesContainer from './containers/TradesContainer';
import PlayerBotDetailsContainer from './containers/PlayerBotDetailsContainer';

ReactDOM.render(
  <Provider store={store}>
    <div style={{ height: '100%' }}>
        <Router history={browserHistory}>
            <Route path="/" exact component={Main}/>
            <Route path="/bots" component={BotsContainer}/>
            <Route path="/bot/:botName" component={PlayerBotDetailsContainer}/>
            <Route path="/trades" component={TradesContainer}/>
        </Router>
     </div>
  </Provider>,
  document.getElementById('root')
);
