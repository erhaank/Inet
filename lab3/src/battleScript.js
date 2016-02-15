/*jshint globalstrict: true*/
/*global $:false */
"use strict";
var size = 9;
var boatsLeft1 = 0;
var boatsLeft2 = 0;
var imgMatrix1, imgMatrix2;
var hitMatrix1, hitMatrix2;
var p1, p2;


var createBox = function(src, hitMatrix, x, y) {
	var img = $("<img/>");
	img.attr("src", src);
	img.attr("class", "box");
	img.click(function() {
		if (hitMatrix[x][y] === 0) { // Miss
			img.attr("src", "miss.png");
		} else { // Hit
			img.attr("src", "hit.png");
			boatsLeft--;
			if (boatsLeft === 0) { // Finished
				$("body").append("<p>All boats have been hit! Good job! :)</p>");
			}
		}
	});
	return img;
};

function startup() {
	imgMatrix1 = new Array(size);
	hitMatrix1 = new Array(size);
	imgMatrix2 = new Array(size);
	hitMatrix2 = new Array(size);

	for (var i = 0; i < size; i++) {
		imgMatrix1[i] = new Array(size);
		hitMatrix1[i] = new Array(size);
		imgMatrix2[i] = new Array(size);
		hitMatrix2[i] = new Array(size);
	}

	p1 = $("#player1");
	p2 = $("#player2");

	setupPlayer(p1,1);
	setupPlayer(p2,2);
}

function setupPlayer(player, id) {
	var hitMatrix = setupHitMatrix(id);
	var imgMatrix;
	if (id === 1)
		imgMatrix = imgMatrix1;
	else
		imgMatrix = imgMatrix2;

	for (var i = 0; i < size; i++) {
		var div = $("<div></div>");
		for (var j = 0; j < size; j++) {
			imgMatrix[i][j] = createBox("ocean.png", hitMatrix, i, j);
			div.append(imgMatrix[i][j]);
		}
		player.append(div);
	}
}

function setupHitMatrix(player) {
	// The hitMatrix keeps track of where the boats are.
	// A 0 indicates water, 1 indicates a boat.
	var hitMatrix;
	if (player === 1)
		hitMatrix = hitMatrix1;
	else
		hitMatrix = hitMatrix2;

	//Start by setting all values in the matrix to 0
	for (var i = 0; i < size; i++) {
		for (var j = 0; j < size; j++) {
			hitMatrix[i][j] = 0;
		}
	}

	// Manual boat setup
	if (player === 1) {
		hitMatrix[0][0] = 1; boatsLeft1++;
		hitMatrix[0][1] = 1; boatsLeft1++;
		hitMatrix[0][2] = 1; boatsLeft1++;
		hitMatrix[4][5] = 1; boatsLeft1++;
		hitMatrix[5][5] = 1; boatsLeft1++;
		hitMatrix[6][5] = 1; boatsLeft1++;
	}

	if (player === 2) {
		hitMatrix[7][0] = 1; boatsLeft2++;
		hitMatrix[7][1] = 1; boatsLeft2++;
		hitMatrix[7][2] = 1; boatsLeft2++;
		hitMatrix[4][2] = 1; boatsLeft2++;
		hitMatrix[5][2] = 1; boatsLeft2++;
		hitMatrix[6][2] = 1; boatsLeft2++;
	}
	return hitMatrix;
}