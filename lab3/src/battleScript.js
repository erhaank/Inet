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


var SIZE = 9; // TODO change to 9
var AMOUNT_OF_SHIPS = 10;

var player1, player2;

var createBox = function(src, player, position) {
	var img = $("<img/>");
	img.attr("src", src);
	img.attr("class", "box");
	img.click(function() {
		if (player.play) {
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
		} else { // Place ships
			if (img.attr("src") === "ocean.png") {
				img.attr("src", "hit.png");
				player.ships[player.shipAmount] = position;
				player.shipAmount++;
				if (player.shipAmount === AMOUNT_OF_SHIPS)
					finishedPlacement(player);
			} else {
				img.attr("src", "ocean.png");
				for (var i = 0; i < player.ships.length; i++) {
					var ship = player.ships[i];
					if (ship.x === position.x && ship.y === position.y) {
						while (i < player.ships.length-1) {
							player.ships[i] = player.ships[i+1];
							i++;
						}
						i++;
					}
				};
				player.shipAmount--;
			}
			player.scoreboard.placed.text("Placed ships: "+player.shipAmount);
		}
	});
	return img;
};

function startup() {
	player1 = new player(1, $("#player1"), new scoreboard(), new Array(AMOUNT_OF_SHIPS), newMatrix());
	player2 = new player(2, $("#player2"), new scoreboard(), new Array(AMOUNT_OF_SHIPS), newMatrix());

	setupPlayer(player1);
	setupPlayer(player2);
	setResetButton();
}

function setupPlayer(player) {
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

function shoot(player, position) {
	for (var i = 0; i < player.ships.length; i++) {
		var ship = player.ships[i];
		if (position.x === ship.x && position.y === ship.y) {
			player.shipsLeft--;
			player.scoreboard.sunk.text("Sunk ships: "+(AMOUNT_OF_SHIPS-player.shipsLeft));
			return HIT;
		}
	}
	return MISS;
}

function setResetButton(player) {
	var button = $("#resetButton");
	button.click(function() {
		resetPlayer(player1);
		resetPlayer(player2);
	});
}

function resetPlayer (player) {
	player.div.empty();
	player.scoreboard = new scoreboard();
	player.div.append(player.scoreboard.div);
	player.shots = 0;
	player.finished = 0;
	player.play = false;
	player.shipAmount = 0;
	setupPlayer(player);
}

function finishedPlacement(player) {
	player.play = true;
	for (var i = 0; i < SIZE;i++) {
		for (var j = 0; j < SIZE; j++) {
			player.imgMatrix[i][j].attr("src", "ocean.png");
		}
	}
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
	this.shipAmount = 0;
	this.imgMatrix = imgmatrix;
	this.shipsLeft = AMOUNT_OF_SHIPS;
	this.shots = 0;
	this.finished = 0;
	this.play = false;

	this.div.append(this.scoreboard.div);
}

// The scoreboard of each player
function scoreboard() {
	this.div = $("<div/>");
	this.head = $("<h1/>")
	this.placed = $("<p>Placed ships: "+0+"</p>");
	this.sunk = $("<p>Sunk ships: 0</p>");
	this.shots = $("<p>Total shots: 0</p>");
	this.div.append(this.placed);
	this.div.append(this.sunk);
	this.div.append(this.shots);
}