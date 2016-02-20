<html>
<head>
<link rel="stylesheet" type="text/css" href="bostad.css">
</head>
<body>

<?php 
$user="engeli_admin";
$password="197n99Kk";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=engeli;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$stmt = $db->prepare("SELECT * FROM bostader WHERE
	(adress LIKE CONCAT('%',:adr,'%') OR :adr = '') AND
	lan = :lan AND
	(objekttyp = :typ1 OR objekttyp = :typ2) AND
	area >= :min_area AND (area <= :max_area OR :max_area = 0) AND
	rum >= :min_rum AND (rum <= :max_rum OR :max_rum = 0) AND
	pris >= :min_pris AND (pris <= :max_pris OR :max_pris = 0) AND
	avgift >= :min_avgift AND (avgift <= :max_avgift OR :max_avgift = 0)");

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

$stmt->execute(); 

echo "<table id=bostader>";
echo "<tr>
	<th>Län</th>
	<th>Typ</th>
	<th>Adress</th>
	<th>Area</th>
	<th>Rum</th>
	<th>Pris</th>
	<th>Avgift</th>
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
 ?>

</body>
</html>

