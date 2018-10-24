import React, { Component } from 'react';
import moment from 'moment';
import randomColor from 'random-color';

import { VictoryChart, VictoryLine, VictoryArea, VictoryLegend } from 'victory';

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

		this.max_health_points = 0;

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
				if (c.health_points > this.max_health_points) {
					this.max_health_points = c.health_points;
				}
				if (nextCastles[c.id]) {
					if (c.health_points >= 0) {
						nextCastles[c.id].health_points.push({ x: data.time, y: c.health_points, y0: 0 });
					}
					if (c.max_health_points >= 0) {
						nextCastles[c.id].max_health_points.push({ x: data.time, y: c.max_health_points, y0: 0 });
					}
					nextCastles[c.id].gas.push({ x: data.time, y: c.gas, y0: 0 });
					nextCastles[c.id].max_gas.push({ x: data.time, y: c.max_gas, y0: 0 });
					if (c.health_points <= 0) {
						nextCastles[c.id].death_time = data.time;
					}
					return;
				}
				nextCastles[c.id] = Object.assign({}, c, {
					health_points: [{ x: data.time, y: c.health_points, y0: 0 }],
					max_health_points: [{ x: data.time, y: c.max_health_points, y0: 0 }],
					gas: [{ x: data.time, y: c.gas, y0: 0 }],
					max_gas: [{ x: data.time, y: c.max_gas, y0: 0 }],
					color: randomColor().hexString(),
					color2: randomColor().hexString(),
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

		if (this.state.focusKingdom) {
			this.setState({ focusKingdom: null });
		}

		if (id === this.state.focus) {
			return this.setState({ focus: null });
		}

		this.setState({ focus: id });
	}

	focusKingdom = id => e => {
		e.preventDefault();

		if (this.state.focus) {
			this.setState({ focus: null });
		}

		if (id === this.state.focusKingdom) {
			return this.setState({ focusKingdom: null });
		}

		this.setState({ focusKingdom: id });
	}

	expand = (what) => () => this.setState({ [what]: !this.state[what] })

	reset = () => {
		this.setState({
			kingdoms: {},
			castles: {},
			time: 0
		});
	}

	render = () =>  {
		const { loading, kingdoms, castles, time, focus, focusKingdom } = this.state;

		if (loading) {
			return <div className="App"><h1>Loading...</h1></div>;
		}

		const castleList = Object.values(castles);

		const kingdomList = Object.values(kingdoms).map(k => (
			<li key={k.id} className={k.id === focusKingdom ? 'text-primary' : null}>
				<span style={{cursor:'pointer'}} onClick={this.focusKingdom(k.id)}>
					{castleList.find(c => c.kingdom === k.id && !c.death_time) ? k.name : <s>{k.name}</s>}
				</span>
			</li>
		));

		const castleListComponent = castleList.map(c => (
			<li key={c.id} className={focus === c.id || c.kingdom === focusKingdom ? 'text-primary' : null}>
				<span style={{cursor:'pointer'}} onClick={this.focusCastle(c.id)}>[{kingdoms[c.kingdom].name}] {c.death_time ? <s>{c.name}</s> : c.name}</span>
				&nbsp;<i style={{backgroundColor: c.color}}>&nbsp;&nbsp;&nbsp;&nbsp;</i>
				{' '}{/*<br/>*/}
				<small>HP: <span className="text-muted">{Math.round(c.health_points[c.health_points.length - 1].y / c.max_health_points[c.max_health_points.length - 1].y * 100)}%</span></small>
				{' '}
				<small>Gas: <span className="text-muted">{Math.round(c.gas[c.gas.length - 1].y / c.max_gas[c.max_gas.length - 1].y * 100)}%</span></small>
				{c.death_time && ' '}
				{c.death_time && <small>DT: <span className="text-muted">{moment.duration(c.death_time, 'minutes').humanize()}</span></small>}
			</li>
		));

		const healthLines = (() => {
			if (focus) {
				return [
					<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_health_points} />,
					<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].health_points} />,
				]
			}
			const filteredCastles = (() => {
				if (focusKingdom) {
					return castleList.filter(c => c.kingdom === focusKingdom);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine key={c.id} style={{ data: { stroke: c.color } }} data={c.health_points} />
			));
		})();

		const deathLines = castleList.filter(c => c.death_time).map(c => (
			<VictoryLine key={`death-line-${c.id}`} style={{ data: { stroke: c.color, strokeWidth: 4 } }} data={[{ x: c.death_time, y: 0 }, { x: c.death_time, y: this.max_health_points }]} />
		));

		const gasLines = (() => {
			if (focus) {
				return [
					<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_gas} />,
					<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].gas} />,
				];
			}
			const filteredCastles = (() => {
				if (focusKingdom) {
					return castleList.filter(c => c.kingdom === focusKingdom);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine key={c.id} style={{ data: { stroke: c.color } }} data={c.gas} />
			));
		})();

		return (
			<div className="container">
				<h1>Time: {moment.duration(time, 'minutes').humanize()}&nbsp;
				<button onClick={this.reset} className="btn btn-outline-dark btn-sm">Reset</button>
				</h1>
				<div className="row">
					<div className="col-sm-9">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Castles</h5>
								<ul className="list-unstyled">{castleListComponent}</ul>
							</div>
						</div>
					</div>
					<div className="col-sm-3">
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Kingdoms</h5>
								<ul className="list-unstyled">{kingdomList}</ul>
							</div>
						</div>
					</div>
				</div>
				<div className="row">
					<div className={`col-sm-${this.state.health_points ? 12 : 6}`}>
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Health Points&nbsp;
									<button onClick={this.expand('health_points')} className="btn btn-outline-dark btn-sm">{this.state.health_points ? 'Shrink' : 'Expand'}</button>
								</h5>
								<VictoryChart animate={{ duration: 1000, easing: "bounce" }}>
									{healthLines}
									{deathLines}
								</VictoryChart>
							</div>
						</div>
					</div>
					<div className={`col-sm-${this.state.gas ? 12 : 6}`}>
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Gas&nbsp;
									<button onClick={this.expand('gas')} className="btn btn-outline-dark btn-sm">{this.state.gas ? 'Shrink' : 'Expand'}</button>
								</h5>
								<VictoryChart
									animate={{ duration: 1000, easing: "bounce" }}
									padding={{ top: 50, left: 60, right: 50, bottom: 50 }}>
									{gasLines}
								</VictoryChart>
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default App;
