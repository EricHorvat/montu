import React, { Component } from 'react';
import './App.css';

import { VictoryChart, VictoryLine } from 'victory';

class App extends Component {
	state = {
		loading: true,
		kingdoms: {},
		castles: {},
		time: 10
	}

	componentWillMount = () => {
		this.socket = new WebSocket("ws://localhost:1337");

		this.socket.onopen = () => {
			this.setState({ loading: false });
		}

		this.socket.onmessage = (event) => {
			const data = JSON.parse(event.data);
			const { kingdoms, castles } = this.state;

			let nextKingdoms = kingdoms;

			if (!Object.keys(kingdoms).length) {
				nextKingdoms = Object.assign({}, kingdoms);
				data.kingdoms.forEach(k => {
					nextKingdoms[k.id] = k;
				});
			}

			this.setState({
				kingdoms: nextKingdoms
			});

		}
	}

	render = () =>  {
		const { loading, kingdoms, castles, time } = this.state;

		if (loading) {
			return <div className="App"><h1>Loading...</h1></div>;
		}

		const kingdomList = Object.values(kingdoms).map(k => (
			<li key={k.id}>{k.name}</li>
		));

		const castleList = Object.values(castles).map(c => (
			<li key={c.id}>{c.name}</li>
		));

		return (
			<div className="App">
				<h1>Time: {time}</h1>
				<div>
					<h3>Kingdoms</h3>
					<ul>{kingdomList}</ul>
				</div>
				<div>
					<h3>Castles</h3>
					<ul>{castleList}</ul>
				</div>
				{/*<VictoryChart theme={VictoryTheme.material}>
					<VictoryLine data={} />
		</VictoryChart>*/}
			</div>
		);
	}
}

export default App;
