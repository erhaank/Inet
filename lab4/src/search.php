<html>
<head>
<link rel="stylesheet" type="text/css" href="bostad.css">
<script src="jquery-1.12.0.min.js"></script>
<script src="bostad.js"></script>
</head>

<body>
<?php
//$user="engeli_admin";
$user="agnesam_admin";
//$password="197n99Kk";
$password="FfXD1Ehl";
//$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=engeli;charset=utf8', $user, $password);
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

if(isset($_COOKIE["lab4"])) {
    echo $_COOKIE["lab4"];
} 

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

echo "<select name='lan' form='myForm'>";
$stmt = $db->query("SELECT DISTINCT lan FROM bostader");
while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    echo "<option value='".$row['lan']."'>".$row['lan']."</option>";
}
echo "</select>";
 ?>
<form id="myForm" accept-charset="utf-8">

<!--Län: <input type="text" name="lan"><br>-->
Objekttyp: 
<div id="objekttyp">
	<input type="checkbox" name="villa" value="Villa">Villa<br>
	<input type="checkbox" name="bostadsratt" value="Bostadsrätt">Bostadsrätt<br> 
</div>
Adress: <input type="text" name="adress"><br>
Min area: <input type="number" name="min_area"><br>
Max area: <input type="number" name="max_area"><br>
Min rum: <input type="number" name="min_rum"><br>
Max rum: <input type="number" name="max_rum"><br>
Min pris: <input type="number" name="min_pris"><br>
Max pris: <input type="number" name="max_pris"><br>
Min avgift: <input type="number" name="min_avgift"><br>
Max avgift: <input type="number" name="max_avgift"><br>

<input type="submit" value="Search">

</form>

<div id='result'></div>
</body>
</html>
