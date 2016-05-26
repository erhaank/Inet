<?php 
ob_start();
$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);


// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$type = $_POST["type"];
$user = $_POST["username"];

$query = "select name from {$type} where userId='{$user}'";

$stmt = $db->prepare($query);

$stmt->execute();
$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

foreach ($rows as $row) {
	echo "<option value='{$row['name']}'>{$row['name']}</option>";
}
?>