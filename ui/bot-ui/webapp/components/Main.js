import React from 'react';
import { Link } from 'react-router-dom';
import * as _ from 'lodash';

export default class Main extends React.Component {

    componentDidMount() {
        if(_.isFunction(this.props.onMountCallback)) {
            this.props.onMountCallback();
        }
    }

    render() {
        console.log("Main hit");
        return (
<ul>
    <li>
      <Link to="/bots">Bots</Link>
    </li>
    <li>
      <Link to="/trades">Trades</Link>
    </li>
  </ul>
        );
    }
}
