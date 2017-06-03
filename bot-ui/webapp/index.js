import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, browserHistory } from 'react-router'
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import 'babel-polyfill';

import rootReducer from './redux/reducers';

const middleware = applyMiddleware(thunk);

store = createStore(
    rootReducer,
    middleware
);

//End boilerplate
//Components

import Main from './components/Main';

ReactDOM.render(
    <Provider store={ store }>
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
