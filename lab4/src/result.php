<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="bostad.css">
<script src="jquery-1.12.0.min.js"></script>
<script>
function sort(order_by) {
	var adress = $("#adress").val();
	var lan  = $("#lan").val();
	var min_pris = $("#min_pris").val();
	var max_pris = $("#max_pris").val();
	var min_avgift = $("#min_avgift").val();
	var max_avgift = $("#max_avgift").val();
	var min_area = $("#min_area").val();
	var max_area = $("#max_area").val();
	var min_rum = $("#min_rum").val();
	var max_rum = $("#max_rum").val();
	var typ1 = $("#typ1").val();
	var typ2 = $("#typ2").val();
        
	$("#results").load('result.php', 
		{"typ1":typ1});
	/*	"lan":lan,
		"min_pris":min_pris,
		"max_pris":max_pris,
		"min_avgift":min_avgift,
		"max_avgift":max_avgift,
		"min_area":min_area,
		"max_area":max_area,
		"min_rum":min_rum,
		"max_rum":max_rum,
		"typ1":typ1,
		"typ2":typ2,
		"order_variable":order_by} );	
*/}
</script>
</head>
<body>

<div id = "results">
<?php 
$user="engeli_admin";
$password="197n99Kk";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=engeli;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$query = "SELECT * FROM bostader WHERE
	(adress LIKE CONCAT('%',:adr,'%') OR :adr = '') AND
	lan = :lan AND
	(objekttyp = :typ1 OR objekttyp = :typ2) AND
	area >= :min_area AND (area <= :max_area OR :max_area = 0) AND
	rum >= :min_rum AND (rum <= :max_rum OR :max_rum = 0) AND
	pris >= :min_pris AND (pris <= :max_pris OR :max_pris = 0) AND
	avgift >= :min_avgift AND (avgift <= :max_avgift OR :max_avgift = 0)

	ORDER BY :order_variable";

$stmt = $db->prepare($query);

$stmt->bindParam(':adr', $adress);
$stmt->bindParam(':lan', $lan);
$stmt->bindParam(':typ1', $typ1);
$stmt->bindParam(':typ2', $typ2);
$stmt->bindParam(':min_area', $min_area);
$stmt->bindParam(':max_area', $max_area);
$stmt->bindParam(':min_rum', $min_rum);
$stmt->bindParam(':max_rum', $max_rum);
$stmt->bindParam(':min_pris', $min_pris);
$stmt->bindParam(':max_pris', $max_pris);
$stmt->bindParam(':min_avgift', $min_avgift);
$stmt->bindParam(':max_avgift', $max_avgift);
$stmt->bindParam(':order_variable', $order_variable);

$adress = $_POST["adress"];
$lan = $_POST["lan"];
$typ1 = $_POST["villa"];
$typ2 = $_POST["bostadsratt"];
$min_area = $_POST["min_area"];
$max_area = $_POST["max_area"];
$min_rum = $_POST["min_rum"];
$max_rum = $_POST["max_rum"];
$min_pris = $_POST["min_pris"];
$max_pris = $_POST["max_pris"];
$min_avgift = $_POST["min_avgift"];
$max_avgift = $_POST["max_avgift"];
$order_variable = "pris";
$tmp = $_POST["order_variable"];
if ($tmp != null)
	$order_variable = $tmp;	


$stmt->execute(); 

echo "<table id='bostader'>";
echo "<tr>
	<th onclick='sort(\"lan\")'>Län</th>
	<th onclick='sort(\"objekttyp\")'>Typ</th>
	<th onclick='sort(\"adress\")'>Adress</th>
	<th onclick='sort(\"area\")'>Area</th>
	<th onclick='sort(\"rum\")'>Rum</th>
	<th onclick='sort(\"pris\")'>Pris</th>
	<th onclick='sort(\"avgift\")'>Avgift</th>
      </tr>";

while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
	$values = array(
		$row['lan'], 
		$row['objekttyp'], 
		$row['adress'],
		$row['area'],
		$row['rum'],
		$row['pris'],
		$row['avgift']);
	echo "<tr>";
	foreach($values as $val) {
		echo "<td>$val</td>";
	}
	echo "</tr>";
}
echo "</table>";

echo "<p id='adress' class='hidden' style=\"font-size:0%;\">".$adress."</p>";
echo "<p id='min_pris' class='hidden' style=\"font-size:0%;\">".$min_pris."</p>";
echo "<p id='max_pris' class='hidden' style=\"font-size:0%;\">".$max_pris."</p>";
echo "<p id='lan' class='hidden' style=\"font-size:0%;\">".$lan."</p>";
echo "<p id='min_avgift' class='hidden' style=\"font-size:0%;\">".$min_avgift."</p>";
echo "<p id='max_avgift' class='hidden' style=\"font-size:0%;\">".$max_avgift."</p>";
echo "<p id='min_rum' class='hidden' style=\"font-size:0%;\">".$min_rum."</p>";
echo "<p id='max_rum' class='hidden' style=\"font-size:0%;\">".$max_rum."</p>";
echo "<p id='typ1' class='hidden' style=\"font-size:0%;\">".$typ1."</p>";
echo "<p id='typ2' class='hidden' style=\"font-size:0%;\">".$typ2."</p>";
echo "<p id='min_area' class='hidden' style=\"font-size:0%;\">".$min_area."</p>";
echo "<p id='max_area' class='hidden' style=\"font-size:0%;\">".$max_area."</p>";
//TODO!!
function run($sortVal = 'pris') {
	
}
 ?>
</div>
</body>
</html>

