<html>
<head>
<link rel="stylesheet" type="text/css" href="bostad.css">
<script src="jquery-1.12.0.min.js"></script>
<script src="bostad.js"></script>
</head>

<body>
<div id="formDiv">
<?php
//$user="engeli_admin";
$user="agnesam_admin";
//$password="197n99Kk";
$password="FfXD1Ehl";
//$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=engeli;charset=utf8', $user, $password);
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$cookie_value = "";

if(isset($_COOKIE["lab4"])) {
    $cookie_value = $_COOKIE["lab4"];
    echo "<script>
	$(document).ready(function() {
		$('#searchparams').text('" . $cookie_value . "');
		$.ajax(
			{
			type: 'POST',
			url: 'result.php',
			data: '" . $cookie_value . "',
			success: function(result) {
				$('#result').html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		$('#result').show();
	});
	</script>";
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
<div id="objekttyp">
	<label for="villa" class="villa">Villa</label>
	<label for="bostadsratt" class="bostadsratt">Bostadsrätt</label><br>
	<input type="checkbox" class="villa" name="villa" value="Villa">
	<input type="checkbox" class="bostadsratt" name="bostadsratt" value="Bostadsrätt"> 
</div> <br>

<label>Adress:</label>
<input type="text" name="adress"><br>
<label>Min area:</label>
<input type="number" name="min_area"><br>
<label>Max area:</label>
<input type="number" name="max_area"><br>
<label>Min rum:</label>
<input type="number" name="min_rum"><br>
<label>Max rum:</label>
<input type="number" name="max_rum"><br>
<label>Min pris:</label>
<input type="number" name="min_pris"><br>
<label>Max pris:</label>
<input type="number" name="max_pris"><br>
<label>Min avgift:</label>
<input type="number" name="min_avgift"><br>
<label>Max avgift:</label>
<input type="number" name="max_avgift"><br>

<input type="submit" value="Search">
</form>
</div>
<div id='result'></div>
<p id='searchparams'></p>
</body>
</html>


