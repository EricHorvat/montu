const Enquirer = require('enquirer')
		, enquirer = new Enquirer()
		, crypto = require('crypto')
		, yaml = require('write-yaml')
		, log = require('log-utils')
		, prettyjson = require('prettyjson');

enquirer.register('confirm', require('prompt-confirm'));

const isNumber = v => !isNaN(parseFloat(v)) && isFinite(v);
const isNonNegativeNumber = v => isNumber(v) && parseInt(v, 10) > 0;
const isNonNegativeNumberOrZero = v => isNumber(v) && parseInt(v, 10) >= 0;

const times = n => new Array(n).fill(undefined);
const range = (n, b = 0) => times(n).map(($, i) => i + b);

const randomInt = (a, b) => {
	const buffer = crypto.randomBytes(8);
	const value = parseInt(buffer.toString('hex'), 16);
	return Math.min(a, b) + (value % (Math.abs(b - a) + 1));
}

const MINUTES_IN_A_MONTH = 30 * 24 * 60;

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
	'kingdom_count',
	'min_castles',
	'max_castles',
	'filename'
]).then(config => {

	if (!config.environment.seed) {
		config.environment.seed = Math.round(Math.random() * 10000);
	}

	config.environment.time *= MINUTES_IN_A_MONTH;

	config.kingdoms = range(config.kingdom_count, 1).map(i => ({
		name: `Kingdom ${i}`,
		offenseCapacity: randomInt(1, 100),
		castles: range(randomInt(config.min_castles, config.max_castles)).map(j => ({
			characteristics: {
				viewDistance: randomInt(1, 100),
				attackDistance: randomInt(1, 100),
				healthPoints: randomInt(1, 100),
				attack: randomInt(1, 100)
			},
			location: {
				lat: randomInt(0, config.environment.size),
				lng: randomInt(0, config.environment.size)
			}
		}))
	}));

	delete config.min_castles;
	delete config.max_castles;
	delete config.seed;
	delete config.kingdom_count;
	delete config['environment.size'];
	delete config['environment.time'];
	delete config['environment.seed'];

	const filename = /\.ya?ml$/.test(config.filename) ? config.filename : `${config.filename}.yaml`;
	delete config.filename;

	yaml.sync(filename, config);

	console.log(log.heading(`${log.symbol.success} Successfully wrote to ${filename}`));
	console.log(prettyjson.render(config));
	
});