import React, { Component } from 'react';
import moment from 'moment';
import momentDurationSetup from 'moment-duration-format';
import randomColor from 'random-color';

import { VictoryChart, VictoryLine, VictoryArea, VictoryAxis, VictoryLabel, VictoryScatter } from 'victory';

momentDurationSetup(moment);

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
		this.max_gas = 0;

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
				if (c.gas > this.max_gas) {
					this.max_gas = c.gas;
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
					if (c.health_points <= 5) {
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

		const kingdomList = Object.values(kingdoms).map(k => {
			const total = castleList.filter(c => c.kingdom === k.id);
			const alive = total.filter(c => !c.death_time).length;
			return (
				<li key={k.id} className={k.id === focusKingdom ? 'text-primary' : null}>
					<span style={{cursor:'pointer'}} onClick={this.focusKingdom(k.id)}>
						{alive ? k.name : <s>{k.name}</s>} ({alive}/{total.length})
					</span>
				</li>
			)
		});

		const castleListComponent = castleList.map(c => (
			<li key={c.id} className={focus === c.id || c.kingdom === focusKingdom ? 'text-primary' : null}>
				<span style={{cursor:'pointer'}} onClick={this.focusCastle(c.id)}>[{kingdoms[c.kingdom].name}] {c.death_time ? <s>{c.name}</s> : c.name}</span>
				&nbsp;<i style={{backgroundColor: c.color}}>&nbsp;&nbsp;&nbsp;&nbsp;</i>
				{' '}{/*<br/>*/}
				<small>HP: <span className="text-muted">{Math.round(c.health_points[c.health_points.length - 1].y / c.max_health_points[c.max_health_points.length - 1].y * 100)}%</span></small>
				{' '}
				<small>Gas: <span className="text-muted">{Math.round(c.gas[c.gas.length - 1].y / c.max_gas[c.max_gas.length - 1].y * 100)}%</span></small>
				{c.death_time && ' '}
				{c.death_time && <small>DT: <span className="text-muted">{moment.duration(c.death_time, 'minutes').format()}</span></small>}
			</li>
		));

		const healthLines = (() => {
			if (focus) {
				return <VictoryLine
					style={{ data: { stroke: castles[focus].color } }}
					data={castles[focus].health_points}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(focus)
						}
					}]} />;
				// return [
				// 	<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_health_points} />,
				// 	<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].health_points} />,
				// ]
			}
			const filteredCastles = (() => {
				if (focusKingdom) {
					return castleList.filter(c => c.kingdom === focusKingdom);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer' } }}
					data={c.health_points}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(c.id),
						}
					}]} />
			));
		})();

		const deathLines = castleList.filter(c => c.death_time).map(c => (
			<VictoryLine key={`death-line-${c.id}`} style={{ data: { stroke: c.color, strokeWidth: 4 } }} data={[{ x: c.death_time, y: 0 }, { x: c.death_time, y: this.max_health_points }]} />
		));

		const gasLines = (() => {
			if (focus) {
				return <VictoryLine
					style={{ data: { stroke: castles[focus].color } }}
					data={castles[focus].gas}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(focus)
						}
					}]} />;
				// return [
				// 	<VictoryArea key="2" style={{ data: { fill: castles[focus].color2 } }} data={castles[focus].max_gas} />,
				// 	<VictoryArea key="1" style={{ data: { fill: castles[focus].color } }} data={castles[focus].gas} />,
				// ];
			}
			const filteredCastles = (() => {
				if (focusKingdom) {
					return castleList.filter(c => c.kingdom === focusKingdom);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer' } }}
					data={c.gas}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(c.id),
						}
					}]} />
			));
		})();

		return (
			<div className="container">
				<h1>Time: {moment.duration(time, 'minutes').format()}&nbsp;
				<button onClick={this.reset} className="btn btn-outline-dark btn-sm">Reset</button>
				</h1>
				<div className="row">
					<div className="col-sm-6">
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
								<h5 className="card-title">Map</h5>
								<VictoryScatter
									style={{
										data: {
											fill: d => d.color,
											opacity: d => Math.max(d.health_points[d.health_points.length - 1].y / d.max_health_points[d.max_health_points.length - 1].y, 0.2)
										},
										parent: {
											fill: '#222'
										}
									}}
									size={d => d.id === focus || d.kingdom === focusKingdom ? 12 : 7}
									data={castleList}
								/>
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
								<VictoryChart
									domain={{ x: [0, time], y: [0, this.max_health_points] }}
									animate={{ duration: 1000, easing: "bounce" }}
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{healthLines}
									{deathLines}
									<VictoryAxis dependentAxis label="Health Points" tickFormat={t => `${(t / 1000).toFixed(1)}k`} axisLabelComponent={<VictoryLabel dy={-20} />}/>
									<VictoryAxis label="Ellapsed Time" tickFormat={t => moment.duration(t, 'minutes').format('Y[y], M[m], D[d], H[h]')}/>
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
									domain={{ x: [0, time], y: [0, this.max_gas] }}
									animate={{ duration: 1000, easing: "bounce" }}
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{gasLines}
									<VictoryAxis dependentAxis label="Gas" tickFormat={t => `${(t / 1000).toFixed(1)}k`} axisLabelComponent={<VictoryLabel dy={-20} />} />
									<VictoryAxis label="Ellapsed Time" tickFormat={t => moment.duration(t, 'minutes').format('Y[y], M[m], D[d], H[h]')}/>
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
