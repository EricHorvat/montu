const argv = process.argv.slice(2)
		, fs = require('fs');

console.log('processing log ' + argv[0]);

var lineReader = require('readline').createInterface({
  input: require('fs').createReadStream(argv[0])
});

let i = 0;

const writeStream = fs.createWriteStream(`${argv[0]}-out.csv`);

writeStream.write([
	'Time',
	'Kingdom Id',
	'Kingdom Name',
	'Kingdom Color',
	'Castle Id',
	'Castle Name',
	'Health Points',
	'Max Health Points',
	'Resources',
	'Max Resources',
	'Attackers',
	'Defenders',
	'Power'
].join(';') + '\n');

lineReader.on('line', line => {
	const json = (() => {
		try {
			return JSON.parse(line);
		} catch (e) {
			return null;
		}
	})();
	if (!json) {
		console.log('could not parse line ' + i);
		return;
	}

	json.castles.forEach(castle => {
	    const k = json.kingdoms.find(k => k.id === castle.kingdom);
		writeStream.write([
			json.time,
			castle.kingdom,
			castle.country,
			k.color,
			castle.id,
			castle.name,
			castle.health_points,
			castle.max_health_points,
			castle.resources,
			castle.max_resources,
			castle.attackers,
			castle.defenders,
			k.power,
		].join(';') + '\n')
	});

	if (i === 0) {
		const ccws = fs.createWriteStream(`${argv[0]}-castle-characteristics.csv`);
		ccws.write([
			'Kingdom Id',
			'Kingdom Name',
			'Kingdom Color',
			'Castle Id',
			'Castle Name',
			'Attack Harm',
			'Attack Distance',
			'Offense Capacity',
			'View Distance',
		].join(';') + '\n');
		json.castles.forEach(castle => {
			ccws.write([
				castle.kingdom,
				castle.country,
				json.kingdoms.find(k => k.id === castle.kingdom).color,
				castle.id,
				castle.name,
				castle.attack_harm,
				castle.attack_distance,
				castle.offense_capacity,
				castle.view_distance,
			].join(';') + '\n')
		});
		ccws.end();
		const kcws = fs.createWriteStream(`${argv[0]}-kingdom-characteristics.csv`);
		kcws.write([
			'Kingdom Id',
			'Kingdom Name',
			'Kingdom Color',
			'Warrior Speed',
			'Offense Capacity',
		].join(';') + '\n');
		json.kingdoms.forEach(kingdom => {
			kcws.write([
				kingdom.id,
				kingdom.name,
				kingdom.color,
				kingdom.warrior_speed,
				kingdom.offense_capacity
			].join(';') + '\n')
		});
		kcws.end();
	}
	
	i++;
});

lineReader.on('close', () => {
	writeStream.end();
});

// process.exit(0);