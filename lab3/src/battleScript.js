/*jshint globalstrict: true*/
/*global $:false */
"use strict";
// Constants
var EMPTY = 0;
var SHIP = 1;
var MISS = 2;
var HIT = 3;
var INTACT = 4;
var DESTROYED = 5;

var SIZE = 3; // TODO change to 9
var AMOUNT_OF_SHIPS = 2;


var player1, player2;


var createBox = function(src, player, position) {
	var img = $("<img/>");
	img.attr("src", src);
	img.attr("class", "box");
	img.click(function() {
		if (img.attr("src") === "ocean.png" && player.finished === 0) {
			player.shots++;
			player.scoreboard.shots.text("Total shots: "+player.shots);
			if (shoot(player, position) === MISS) {
				img.attr("src", "miss.png");
			} else { // Hit
				img.attr("src", "hit.png");
				if (player.shipsLeft === 0) { // Finished
					player.finished = 1;
					player.div.append("<p>All ships have been hit! Good job! :)</p>");
				}
			}
		}
	});
	return img;
};

function startup() {
	player1 = new player(1, $("#player1"), new scoreboard(), new Array(AMOUNT_OF_SHIPS), newMatrix());
	player2 = new player(2, $("#player2"), new scoreboard(), new Array(AMOUNT_OF_SHIPS), newMatrix());

	setupPlayer(player1);
	setupPlayer(player2);
}

function setupPlayer(player) {

	setupShips(player);

	for (var i = 0; i < SIZE; i++) {
		var div = $("<div/>");
		for (var j = 0; j < SIZE; j++) {
			player.imgMatrix[i][j] = createBox("ocean.png", player, {x:i, y:j});
			player.div.append(player.imgMatrix[i][j]);
		}
		player.div.append(div);
	}
}

// Creates a new matrix of SIZE * SIZE
function newMatrix() {
	var matrix = new Array(SIZE);
	for (var i = 0; i < SIZE; i++) {
		matrix[i] = new Array(SIZE);
	}
	return matrix;
}

function setupShips(player) {
	// Manual ship setup
	if (player.id === 1) {
		var s1 = new ship(2);
		s1.positions = [{x:0,y:0}, {x:0,y:1}];
		var s2 = new ship(3);
		s2.positions = [{x:2, y:0}, {x:2, y:1}, {x:2, y:2}];
	}
	if (player.id === 2) {
		var s1 = new ship(2);
		s1.positions = [{x:0,y:0}, {x:0,y:1}];
		var s2 = new ship(3);
		s2.positions = [{x:2, y:0}, {x:2, y:1}, {x:2, y:2}];
	}
	addShipToPlayer(player, [s1,s2]);
}


function shoot(player, position) {
	for (var i = 0; i < player.ships.length; i++) {
		var ship = player.ships[i];
		for (var j = 0; j < ship.positions.length; j++) {
			if (position.x === ship.positions[j].x && position.y === ship.positions[j].y) {
				if (ship.hits[j] === INTACT) {
					ship.hits[j] = DESTROYED;
					ship.partsLeft--;
					if (ship.partsLeft === 0) {
						player.shipsLeft--;
						player.scoreboard.sunk.text("Sunk ships: "+(AMOUNT_OF_SHIPS-player.shipsLeft));
					}
				}
				return HIT;
			}
		}
	}
	return MISS;
}

function addShipToPlayer(player, ships) {
	//TODO: Check if the ships collide
	player.ships = ships;
}

// --------- Objects -----------

// A player has an id, two divs called 'div' (for the actual game) and
// scoreboard, a hitMatrix (TODO change) that recoreds where ships are, and a imdMatrix that
// contains the clickable img tags that make up the grid.
function player(id, div, scoreboard, ships, imgmatrix) {
	this.id = id;
	this.div = div;
	this.scoreboard = scoreboard;
	this.ships = ships;
	this.imgMatrix = imgmatrix;
	this.shipsLeft = AMOUNT_OF_SHIPS;
	this.shots = 0;
	this.finished = 0;

	this.div.append(this.scoreboard.div);
}

// A ship has a length, an array of positions and an array of hits (where the ship has been bombed)
function ship(length) {
	this.length = length;
	this.partsLeft = length;
	this.positions = new Array(length);
	this.hits = new Array(length);
	for (var i = 0; i < length; i++) {
		this.hits[i] = INTACT;
	}
}

// The scoreboard of each player
function scoreboard() {
	this.div = $("<div/>");
	this.placed = $("<p>Placed ships: "+AMOUNT_OF_SHIPS+"</p>");
	this.sunk = $("<p>Sunk ships: 0</p>");
	this.shots = $("<p>Total shots: 0</p>");
	this.div.append(this.placed);
	this.div.append(this.sunk);
	this.div.append(this.shots);
}