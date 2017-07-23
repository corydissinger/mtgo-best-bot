import React from 'react';
import {Link} from 'react-router-dom';
import {Button, Table, Panel} from 'react-bootstrap';
import * as _ from 'lodash';

export default class PlayerBotDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = { selectedScreenTest: "",
                   selectedLifecycleStatus: "",
                   collectionOpen: false,
                   eventsOpen: false,
                   camerasOpen: false };
  }

  componentDidMount() {
    if(_.isEmpty(this.props.bot)) {
        this.props.onMountCallback(this.props.botName);
    }
  }

  render() {
    return (
      <div>
        <h1>{this.props.botName}</h1>
        <h4>Current status: {this.props.bot.status}</h4>

        <form onSubmit={(e) => {
                    e.preventDefault();
                    this.props.handleSubmit(this.state.selectedLifecycleStatus, this.state.selectedScreenTest, this.props.botName);
                }}>
            <select value={this.state.selectedScreenTest} onChange={(e) => { this.setState({ selectedScreenTest: e.target.value }) }}>
                <option value={""}>Choose a screen test</option>
                {_.map(this.props.screenTests, (aScreenTest, index) => {
                    return <option key={index} value={aScreenTest}>{aScreenTest}</option>;
                })}
            </select>

            <select value={this.state.selectedLifecycleStatus} onChange={(e) => { this.setState({ selectedLifecycleStatus: e.target.value }) }}>
                <option value={""}>Choose a lifecycle status</option>
                {_.map(this.props.lifecycleStatuses, (aLifecycleStatus, index) => {
                    return <option key={index} value={aLifecycleStatus}>{aLifecycleStatus}</option>;
                })}
            </select>

            <input type="submit" value="Push Event" />
        </form>

        <hr/>

        <h2>Collection</h2>
        <Button onClick={ () => this.setState({ collectionOpen: !this.state.collectionOpen })}>
            Toggle
        </Button>
        <Panel collapsible expanded={this.state.collectionOpen}>
            <Table striped bordered condensed>
                <thead>
                    <td>Quantity</td>
                    <td>Name</td>
                    <td>Price</td>
                </thead>
                <tbody>
                {_.map(this.props.bot.botCards, (aCard, index) => {
                    return <tr key={index}>
                                <td>{aCard.quantity}</td>
                                <td>{aCard.card.name}</td>
                                <td>{aCard.price}</td>
                           </tr>
                })}
                </tbody>
            </Table>
        </Panel>

        <h2>Cameras</h2>
        <Button onClick={ () => this.setState({ camerasOpen: !this.state.camerasOpen })}>
            Toggle
        </Button>
        <Panel collapsible expanded={this.state.camerasOpen}>
            <ul>
            {_.map(this.props.bot.botCameras, (aBotCamera, index) => {
                return <li key={index}><Link to={"/botcamera/id/" + aBotCamera.id}>Taken {aBotCamera.timeTaken}</Link></li>
            })}
            </ul>
        </Panel>

        <h2>Events</h2>
        <Button onClick={ () => this.setState({ eventsOpen: !this.state.eventsOpen })}>
            Toggle
        </Button>
        <Panel collapsible expanded={this.state.eventsOpen}>
            <ul>
            {_.map(this.props.bot.events, (anEvent, index) => {
                return <li key={index}>{anEvent}</li>
            })}
            </ul>
        </Panel>
      </div>
    )
  }
}