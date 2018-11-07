import React, { Component } from 'react';
import moment from 'moment';
import momentDurationSetup from 'moment-duration-format';
import color from 'color';

import { VictoryChart, VictoryLine, VictoryAxis, VictoryLabel, VictoryScatter } from 'victory';

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
		this.max_resources = 0;
		this.max_warriors = 0;

		this.socket.onmessage = (event) => {
			const data = JSON.parse(event.data);

			if (data.init) {
				this.setState({
					kingdoms: {},
					castles: {},
					time: 0
				});
				return;
			}

			const { kingdoms, castles } = this.state;

			let nextKingdoms = kingdoms;

			if (!Object.keys(kingdoms).length) {
				nextKingdoms = Object.assign({}, kingdoms);
				data.kingdoms.forEach(k => {
					nextKingdoms[k.id] = k;
				});
			}

			const nextCastles = Object.assign({}, castles);

			data.castles.forEach((c, i) => {
				if (c.health_points > this.max_health_points) this.max_health_points = c.health_points;
				if (c.resources > this.max_resources) this.max_resources = c.resources;
				if (c.warriors > this.max_warriors) this.max_warriors = c.warriors;
				if (nextCastles[c.id]) {
					if (c.health_points >= 0) {
						nextCastles[c.id].health_points.push({ x: data.time, y: c.health_points, y0: 0 });
					}
					if (c.max_health_points >= 0) {
						nextCastles[c.id].max_health_points.push({ x: data.time, y: c.max_health_points, y0: 0 });
					}
					nextCastles[c.id].resources.push({ x: data.time, y: c.resources, y0: 0 });
					nextCastles[c.id].max_resources.push({ x: data.time, y: c.max_resources, y0: 0 });
					nextCastles[c.id].warriors.push({ x: data.time, y: c.warriors, y0: 0 });
					// nextCastles[c.id].available_warriors.push({ x: data.time, y: c.available_warriors, y0: 0 });
					nextCastles[c.id].attackers.push({ x: data.time, y: c.attackers, y0: 0 });
					// nextCastles[c.id].available_attackers.push({ x: data.time, y: c.available_attackers, y0: 0 });
					nextCastles[c.id].defenders.push({ x: data.time, y: c.defenders, y0: 0 });
					// nextCastles[c.id].available_defenders.push({ x: data.time, y: c.available_defenders, y0: 0 });
					if (c.health_points <= 5) {
						nextCastles[c.id].death_time = data.time;
					}
					return;
				}
				nextCastles[c.id] = Object.assign({}, c, {
					health_points: [{ x: data.time, y: c.health_points, y0: 0 }],
					max_health_points: [{ x: data.time, y: c.max_health_points, y0: 0 }],
					resources: [{ x: data.time, y: c.resources, y0: 0 }],
					max_resources: [{ x: data.time, y: c.max_resources, y0: 0 }],
					warriors: [{ x: data.time, y: c.warriors }],
					// available_warriors: [{ x: data.time, y: c.available_warriors }],
					defenders: [{ x: data.time, y: c.defenders }],
					// available_defenders: [{ x: data.time, y: c.available_defenders }],
					attackers: [{ x: data.time, y: c.attackers }],
					// available_attackers: [{ x: data.time, y: c.available_attackers }],
					color: color(nextKingdoms[c.kingdom].color).darken(i / (data.castles.length || 1)).hex(),
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
						<i style={{backgroundColor: k.color}}>&nbsp;&nbsp;&nbsp;&nbsp;</i>&nbsp;
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
				<small>Resources: <span className="text-muted">{Math.round(c.resources[c.resources.length - 1].y / c.max_resources[c.max_resources.length - 1].y * 100)}%</span></small>
				{c.death_time && ' '}
				{c.death_time && <small>DT: <span className="text-muted">{moment.duration(c.death_time, 'minutes').format()}</span></small>}
			</li>
		));

		const healthLines = (() => {
			const filteredCastles = (() => {
				if (focusKingdom || focus) {
					return castleList.filter(c => c.kingdom === focusKingdom || c.id === focus);
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

		const deathLines = castleList.filter(c => c.death_time).map(c => max => (
			<VictoryLine key={`death-line-${c.id}`} style={{ data: { stroke: c.color, strokeWidth: 4 } }} data={[{ x: c.death_time, y: 0 }, { x: c.death_time, y: max }]} />
		));

		const resourcesLines = (() => {
			const filteredCastles = (() => {
				if (focusKingdom || focus) {
					return castleList.filter(c => c.kingdom === focusKingdom || c.id === focus);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer' } }}
					data={c.resources}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(c.id),
						}
					}]} />
			));
		})();

		const warriorLines = (() => {
			const filteredCastles = (() => {
				if (focusKingdom || focus) {
					return castleList.filter(c => c.kingdom === focusKingdom || c.id === focus);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer' } }}
					data={c.warriors}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(c.id),
						}
					}]} />
			));
		})();

		const attackerLines = (() => {
			const filteredCastles = (() => {
				if (focusKingdom || focus) {
					return castleList.filter(c => c.kingdom === focusKingdom || c.id === focus);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer' } }}
					data={c.attackers}
					events={[{
						target: "data",
						eventHandlers: {
							onClick: this.focusCastle(c.id),
						}
					}]} />
			));
		})();

		const defenderLines = (() => {
			const filteredCastles = (() => {
				if (focusKingdom || focus) {
					return castleList.filter(c => c.kingdom === focusKingdom || c.id === focus);
				}
				return castleList;
			})();
			return filteredCastles.map(c => (
				<VictoryLine
					key={c.id}
					style={{ data: { stroke: c.color, cursor: 'pointer', strokeDasharray: '5,5' } }}
					data={c.defenders}
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
											cursor: 'pointer',
											opacity: d => Math.max(d.health_points[d.health_points.length - 1].y / d.max_health_points[d.max_health_points.length - 1].y, 0.2)
										},
										parent: {
											fill: '#222'
										}
									}}
									size={d => d.id === focus || d.kingdom === focusKingdom ? 12 : 7}
									data={castleList}
									events={[{
										target: "data",
										eventHandlers: {
											onClick: (e, d) => this.focusCastle(d.data[d.index].id)(e),
										}
									}]}
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
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{healthLines}
									{deathLines.map(dl => dl(0.1 * this.max_health_points))}
									<VictoryAxis dependentAxis label="Health Points" tickFormat={t => `${(t / 1000).toFixed(1)}k`} axisLabelComponent={<VictoryLabel dy={-20} />}/>
									<VictoryAxis label="Ellapsed Time" tickFormat={t => moment.duration(t, 'minutes').format('Y[y], M[m], D[d], H[h]')}/>
								</VictoryChart>
							</div>
						</div>
					</div>
					<div className={`col-sm-${this.state.resources ? 12 : 6}`}>
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Resources&nbsp;
									<button onClick={this.expand('resources')} className="btn btn-outline-dark btn-sm">{this.state.resources ? 'Shrink' : 'Expand'}</button>
								</h5>
								<VictoryChart
									domain={{ x: [0, time], y: [0, this.max_resources] }}
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{resourcesLines}
									{deathLines.map(dl => dl(0.1 * this.max_resources))}
									<VictoryAxis dependentAxis label="Resources" tickFormat={t => `${(t / 1000).toFixed(1)}k`} axisLabelComponent={<VictoryLabel dy={-20} />} />
									<VictoryAxis label="Ellapsed Time" tickFormat={t => moment.duration(t, 'minutes').format('Y[y], M[m], D[d], H[h]')}/>
								</VictoryChart>
							</div>
						</div>
					</div>
					<div className={`col-sm-${this.state.warriors ? 12 : 6}`}>
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Warriors&nbsp;
									<button onClick={this.expand('warriors')} className="btn btn-outline-dark btn-sm">{this.state.warriors ? 'Shrink' : 'Expand'}</button>
								</h5>
								<VictoryChart
									domain={{ x: [0, time], y: [0, this.max_warriors] }}
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{warriorLines}
									{deathLines.map(dl => dl(0.1 * this.max_warriors))}
									<VictoryAxis dependentAxis label="Warriors" tickFormat={t => Math.round(t)} axisLabelComponent={<VictoryLabel dy={-20} />} />
									<VictoryAxis label="Ellapsed Time" tickFormat={t => moment.duration(t, 'minutes').format('Y[y], M[m], D[d], H[h]')}/>
								</VictoryChart>
							</div>
						</div>
					</div>
					<div className={`col-sm-${this.state.attack_defend ? 12 : 6}`}>
						<div className="card">
							<div className="card-body">
								<h5 className="card-title">Attackers / Defenders&nbsp;
									<button onClick={this.expand('attack_defend')} className="btn btn-outline-dark btn-sm">{this.state.attack_defend ? 'Shrink' : 'Expand'}</button>
								</h5>
								<VictoryChart
									domain={{ x: [0, time], y: [0, this.max_warriors] }}
									padding={{ top: 10, left: 60, right: 50, bottom: 50 }} >
									{attackerLines}
									{defenderLines}
									{deathLines.map(dl => dl(0.1 * this.max_warriors))}
									<VictoryAxis dependentAxis label="Attackers / Defenders" tickFormat={t => Math.round(t)} axisLabelComponent={<VictoryLabel dy={-20} />} />
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
