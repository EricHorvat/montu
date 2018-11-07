const WebSocketServer = require('websocket').server
		, http = require('http')
		, fs = require('fs')
		, moment = require('moment');

const server = http.createServer(function(request, response) {
	// process HTTP request. Since we're writing just WebSockets
	// server we don't have to implement anything.
});
server.listen(1337, () => {
	console.log('listening on port 1337');
});

// create the server
const wsServer = new WebSocketServer({
	httpServer: server
});

wsServer.on('connect', connection => {
	console.log(`connected from ${connection.remoteAddress}`);
});

const connections = [];
let i = 0;

try {
	fs.mkdirSync('logs');
} catch (e) {

}


const createFileStream = () => fs.createWriteStream(`logs/montu-log-${moment().format('DDMMHHmm')}.log`, {
	flags: 'a',
	encoding: null,
	mode: 0666
});

let fileStream = createFileStream();

// WebSocket server
wsServer.on('request', (request) => {
	const connection = request.accept(null, request.origin);

	connection.id = i++;

	connections.push(connection);

	// This is the most important callback for us, we'll handle
	// all messages from users here.
	connection.on('message', (message) => {
		if (message.type === 'utf8') {
			if (message.utf8Data === 'init') {
				fileStream = createFileStream();
				connections.forEach(conn => {
					console.log('sending init to connection ' + conn.id);
					conn.sendUTF(JSON.stringify({ init: 1 }));
				});
			} else {
				fileStream.write(JSON.stringify(message));
				fileStream.write('\n');
				connections.forEach(conn => {
					console.log('sending to connection ' + conn.id);
					conn.sendUTF(message.utf8Data);
				});
			}
		}
	});

	connection.on('close', (connection) => {
		// close user connection
		const idx = connections.findIndex(conn => conn.id === connection.id);
		connections.splice(idx, 1);
	});
});