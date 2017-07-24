import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

const Trades = ({}) => (
  <ul>
    <li>
      <Link to="/bots">Bots</Link>
    </li>
    <li>
      <Link to="/trades">Trades</Link>
    </li>
  </ul>
);

export default Trades;
