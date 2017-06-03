import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Switch, browserHistory } from 'react-router'
import 'babel-polyfill';

//End boilerplate
//Components

import Main from './components/Main';
import Bots from './components/Bots';
import Trades from './components/Trades';

ReactDOM.render(
    <div style={{ height: '100%' }}>
        <Router history={browserHistory}>
            <Route path="/" exact component={Main}/>
            <Route path="/bots" component={Bots}/>
            <Route path="/trades" component={Trades}/>
        </Router>
   </div>,
    document.getElementById('root')
);
