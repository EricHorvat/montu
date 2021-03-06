const Enquirer = require('enquirer')
		, enquirer = new Enquirer()
		, crypto = require('crypto')
		, yaml = require('write-yaml')
		, log = require('log-utils')
		, prettyjson = require('prettyjson')
		, d3 = require('d3-random')
		, faker = require('faker')
		, randomColor = require('random-color');

enquirer.register('confirm', require('prompt-confirm'));

const isNumber = v => !isNaN(parseFloat(v)) && isFinite(v);
const isNonNegativeNumber = v => isNumber(v) && parseInt(v, 10) > 0;
const isNonNegativeNumberOrZero = v => isNumber(v) && parseInt(v, 10) >= 0;

const times = n => new Array(n).fill(undefined);
const range = (n, b = 0) => times(n).map(($, i) => i + b);

// const randomInt = (a, b) => {
// 	const buffer = crypto.randomBytes(8);
// 	const value = parseInt(buffer.toString('hex'), 16);
// 	return Math.min(a, b) + (value % (Math.abs(b - a) + 1));
// }

const MINUTES_IN_A_MONTH = 30 * 24 * 60;
const MINUTES_IN_A_DAY = 24 * 60;

const BASE_WARRIOR_COST = MINUTES_IN_A_DAY * 3;

enquirer.question({
	name: 'environment.size',
	message: 'Map size (in km): ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Map size must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	}
});

enquirer.question({
	name: 'environment.time',
	message: 'Simulation time (in months): ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Simulation time must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	}
});

enquirer.question('seed', 'Pick a random seed', { type: 'confirm' });

enquirer.question({
	name: 'environment.seed',
	message: 'Seed: ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Selected seed must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	},
	when: answers => !answers.seed
});

enquirer.question({
	name: 'viewport.width',
	message: 'Viewport width (in px): ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Map size must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	}
});

enquirer.question({
	name: 'viewport.height',
	message: 'Viewport height (in px): ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Map size must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	}
});

enquirer.question({
	name: 'kingdom_count',
	message: 'Number of kingdoms: ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Number of kingdoms must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v);
	}
});

enquirer.question({
	name: 'min_castles',
	message: 'Minimum number of castles per kingdom: ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Minimum number of castles must be a non-negative number',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumberOrZero(v);
	}
});

enquirer.question({
	name: 'max_castles',
	message: 'Maximum number of castles per kingdom: ',
	transform: v => parseInt(v, 10),
	errorMessage: 'Maximum number of castles must be a non-negative number and greater than min castles',
	validate: function () {
		const v = this.status === 'submitted' ? this.answer : this.ui.rl.line;
		return isNonNegativeNumber(v) && parseInt(v, 10) >= this.answers.min_castles;
	}
});

enquirer.question({
	name: 'filename',
	message: 'Filename: ',
});

enquirer.prompt([
	'environment.size',
	'environment.time',
	'seed',
	'environment.seed',
	'viewport.width',
	'viewport.height',
	'kingdom_count',
	'min_castles',
	'max_castles',
	'filename'
]).then(config => {

	if (!config.environment.seed) {
		config.environment.seed = Math.round(Math.random() * 10000);
	}

	config.environment.time *= MINUTES_IN_A_MONTH;
	config.environment.strategy = 'DOMINATION_BY_OCCUPATION';

	config.viewport.backgroundImage = null;
	config.viewport.enabled = true;
	config.viewport.updateEvery = 1;

	config.stream = {
	    host: '127.0.0.1',
	    port: 1337
	};

	
	config.kingdoms = range(config.kingdom_count, 1).map(i => {
		const offenseCapacity = Math.round(d3.randomUniform(1, 100)());
		return  {
			name: faker.address.country(),
			offenseCapacity,
			color: randomColor().rgbNumber(),
			warriorSpeed: d3.randomNormal(0.12, 0.012)(), // double
			weakFriends: d3.randomUniform()() > 0.5,
			castles: range(Math.round(d3.randomUniform(config.min_castles, config.max_castles)()), 1).map(j => ({
				name: faker.address.city(),
				characteristics: {
					viewDistance: d3.randomUniform(2, 5)(), // double
					attackDistance: d3.randomUniform(1,1.5)(), // double
					healthPoints: Math.round(d3.randomUniform(5000, 10000)()),
					resources: BASE_WARRIOR_COST * Math.round(d3.randomNormal(25, 5)()),
					spawnProbability: d3.randomUniform(0.1, 5)() / 100,
					deaths: Math.round(d3.randomUniform(0, 4)())
				},
				location: {
					lat: d3.randomUniform(0, config.environment.size)(), // double
					lng: d3.randomUniform(0, config.environment.size)() // double
				}
			}))
		}
	});

	config.maxPriority = Number.MAX_SAFE_INTEGER;
	config.minPriorityDistance = 1e-4;
	config.superWarriorProbability = 1e-4;
	config.healthOffensiveRollCoefficient = 0.7;
	
	config.castlePowerCoefficient = 100;
	config.hpPowerCoefficient = 100;
	config.warriorPowerCoefficient = 100;
	config.friendProbability = 0.2;
	config.rivalProbability = 0.6;
	config.updateNegotationTicks = 100;
	config.friendshipTicks = 100;
	config.rivalPriorityCoefficient = 2.5;
	config.friendPriorityCoefficient = 0.4;
	config.resourcesPerMinute = 1;

	delete config.min_castles;
	delete config.max_castles;
	delete config.seed;
	delete config.kingdom_count;
	delete config['environment.size'];
	delete config['environment.time'];
	delete config['environment.seed'];
	delete config['viewport.width'];
	delete config['viewport.height'];

	const filename = /\.ya?ml$/.test(config.filename) ? config.filename : `${config.filename}.yaml`;
	delete config.filename;

	yaml.sync(filename, config);

	console.log(log.heading(`${log.symbol.success} Successfully wrote to ${filename}`));
	console.log(prettyjson.render(config));
	
});