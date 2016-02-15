/*jshint globalstrict: true*/
/*global $:false */
"use strict";
var size = 9;
var player1, player2;


var createBox = function(src, player, x, y) {
	var img = $("<img/>");
	img.attr("src", src);
	img.attr("class", "box");
	img.click(function() {
		if (player.hitMatrix[x][y] === 0) { // Miss
			img.attr("src", "miss.png");
		} else { // Hit
			img.attr("src", "hit.png");
			player.boatsLeft--;
			if (player.boatsLeft === 0) { // Finished
				player.div.append("<p>All boats have been hit! Good job! :)</p>");
			}
		}
	});
	return img;
};

function startup() {
	player1 = new player(1, $("#player1"), newMatrix(), newMatrix(), 0);
	player2 = new player(2, $("#player2"), newMatrix(), newMatrix(), 0);

	setupPlayer(player1);
	setupPlayer(player2);
}

function setupPlayer(player) {
	setupHitMatrix(player);

	for (var i = 0; i < size; i++) {
		var div = $("<div></div>");
		for (var j = 0; j < size; j++) {
			player.imgMatrix[i][j] = createBox("ocean.png", player, i, j);
			player.div.append(player.imgMatrix[i][j]);
		}
		player.div.append(div);
	}
}

function newMatrix() {
	var matrix = new Array(size);
	for (var i = 0; i < size; i++) {
		matrix[i] = new Array(size);
	}
	return matrix;
}

function setupHitMatrix(player) {
	// The hitMatrix keeps track of where the boats are.
	// A 0 indicates water, 1 indicates a boat.

	//Start by setting all values in the matrix to 0
	for (var i = 0; i < size; i++) {
		for (var j = 0; j < size; j++) {
			player.hitMatrix[i][j] = 0;
		}
	}

	// Manual boat setup
	if (player.id === 1) {
		player.hitMatrix[0][0] = 1; player.boatsLeft++;
		player.hitMatrix[0][1] = 1; player.boatsLeft++;
		player.hitMatrix[0][2] = 1; player.boatsLeft++;
		player.hitMatrix[4][5] = 1; player.boatsLeft++;
		player.hitMatrix[5][5] = 1; player.boatsLeft++;
		player.hitMatrix[6][5] = 1; player.boatsLeft++;
	}

	if (player.id === 2) {
		player.hitMatrix[7][0] = 1; player.boatsLeft++;
		player.hitMatrix[7][1] = 1; player.boatsLeft++;
		player.hitMatrix[7][2] = 1; player.boatsLeft++;
		player.hitMatrix[4][2] = 1; player.boatsLeft++;
		player.hitMatrix[5][2] = 1; player.boatsLeft++;
		player.hitMatrix[6][2] = 1; player.boatsLeft++;
	}
}

function player(id, div, hitmatrix, imgmatrix, boatsLeft) {
	this.id = id;
	this.div = div;
	this.hitMatrix = hitmatrix;
	this.imgMatrix = imgmatrix;
	this.boatsLeft = boatsLeft;
}