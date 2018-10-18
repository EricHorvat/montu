import React, { Component } from 'react';
import moment from 'moment';
import randomColor from 'random-color';

import { VictoryChart, VictoryLine, VictoryArea } from 'victory';

class App extends Component {
	state = {
		loading: true,
		kingdoms: {},
		castles: {},
		time: 0
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

			const nextCastles = Object.assign({}, castles);

			data.castles.forEach(c => {
				if (nextCastles[c.id]) {
					if (c.health_points >= 0) {
						nextCastles[c.id].health_points.push({ x: data.time, y: c.health_points, y0: 0 });
					}
					if (c.max_health_points >= 0) {
						nextCastles[c.id].max_health_points.push({ x: data.time, y: c.max_health_points, y0: 0 });
					}
					nextCastles[c.id].gas.push({ x: data.time, y: c.gas, y0: 0 });
					nextCastles[c.id].max_gas.push({ x: data.time, y: c.max_gas, y0: 0 });
					return;
				}
				nextCastles[c.id] = Object.assign({}, c, {
					health_points: [{ x: data.time, y: c.health_points, y0: 0 }],
					max_health_points: [{ x: data.time, y: c.max_health_points, y0: 0 }],
					gas: [{ x: data.time, y: c.gas, y0: 0 }],
					max_gas: [{ x: data.time, y: c.max_gas, y0: 0 }],
					color: randomColor().hexString(),
					color2: randomColor().hexString()
				});
			});

			this.setState({
				kingdoms: nextKingdoms,
				castles: nextCastles,
				time: data.time
			});

		}
	}

	focusCastle = id => e => {
		e.preventDefault();

		if (id === this.state.focus) {
			return this.setState({ focus: null });
		}

		this.setState({ focus: id });
	}

	render = () =>  {
		const { loading, kingdoms, castles, time, focus } = this.state;

		if (loading) {
			return <div className="App"><h1>Loading...</h1></div>;
		}

		const kingdomList = Object.values(kingdoms).map(k => (
			<li key={k.id}>{k.name}</li>
		));

		const castleList = Object.values(castles).map(c => (
			<li key={c.id} className={focus === c.id ? 'text-primary' : null}>
				<span style={{cursor:'pointer'}} onClick={this.focusCastle(c.id)}>{c.name}</span>
				&nbsp;<i style={{backgroundColor: c.color}}>&nbsp;&nbsp;&nbsp;&nbsp;</i>
				<br/>
				<small>HP: <span className="text-muted">{Math.round(c.health_points[c.health_points.length - 1].y / c.max_health_points[c.max_health_points.length - 1].y * 100)}%</span></small>
				{' '}
				<small>Gas: <span className="text-muted">{Math.round(c.gas[c.gas.length - 1].y / c.max_gas[c.max_gas.length - 1].y * 100)}%</span></small>
			</li>
		));

		const healthLines = (() => {
			if (focus) {
				return [
					<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_health_points} />,
					<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].health_points} />,
				]
			}
			return Object.values(castles).map(c => (
				<VictoryLine key={c.id} style={{ data: { stroke: c.color } }} data={c.health_points} />
			))
		})();

		const gasLines = (() => {
			if (focus) {
				return [
					<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_gas} />,
					<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].gas} />,
				];
			}
			return Object.values(castles).map(c => (
				<VictoryLine key={c.id} style={{ data: { stroke: c.color } }} data={c.gas} />
			));
		})();

		return (
			<div className="container">
				<h1>Time: {moment.duration(time, 'minutes').humanize()}</h1>
				<div className="row">
					<div className="col-sm-3">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Kingdoms</h5>
								<ul className="list-unstyled">{kingdomList}</ul>
							</div>
						</div>
					</div>
					<div className="col-sm-3">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Castles</h5>
								<ul className="list-unstyled">{castleList}</ul>
							</div>
						</div>
					</div>
					<div className="col-sm-6">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Health Points</h5>
								<VictoryChart>{healthLines}</VictoryChart>
							</div>
						</div>
					</div>
					<div className="col-sm-6">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Gas</h5>
								<VictoryChart>{gasLines}</VictoryChart>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default App;
