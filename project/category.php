<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
$name = $_POST["category_name"];

$query = "insert into category (name, userid)
	values (:name, :user_id)";

$stmt = $db->prepare($query);

$stmt->bindParam(':user_id', $user_id);
$stmt->bindParam(':name', $name);

$stmt->execute(); 

?>
