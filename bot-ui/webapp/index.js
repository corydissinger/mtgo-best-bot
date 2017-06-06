import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import { createLogger } from 'redux-logger'
import thunkMiddleware from 'redux-thunk'
import reducer from './reducers';

console.log("App started");

(function(proxied) {
    window.fetch = function() {
        if(arguments.length == 1) {
            [].push.call(arguments, {});
        }

        arguments[1] = { ...{ credentials: "include" }, ...arguments[1]};
        console.log("Proxying fetch with args=" + arguments);
        return proxied.apply(this, arguments);
    }
})(window.fetch);

const store = createStore(reducer, applyMiddleware(
    createLogger({ level: 'info', diff: true}),
    thunkMiddleware
));

//End boilerplate
//Components

import MainContainer from './containers/MainContainer';
import BotsContainer from './containers/BotsContainer';
import TradesContainer from './containers/TradesContainer';
import PlayerBotDetailsContainer from './containers/PlayerBotDetailsContainer';

ReactDOM.render(
  <Provider store={store}>
        <Router>
            <div style={{ height: '100%' }}>
                <Route path="/bots" component={BotsContainer}/>
                <Route path="/bot/:botName" component={PlayerBotDetailsContainer}/>
                <Route path="/trades" component={TradesContainer}/>
                <hr />
                <Route component={MainContainer} />
            </div>
        </Router>
  </Provider>,
  document.getElementById('root')
);
