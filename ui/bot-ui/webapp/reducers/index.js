import { combineReducers } from 'redux';
import bots from './bots';
import botDetails from './botDetails';
import system from './system';

const botApp = combineReducers({
  bots,
  botDetails,
  system
});

export default botApp;
