<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$user_id = $_POST["user_id"];

$query = "delete from inProgress where userId = '{$user_id}'";
$stmt = $db->prepare($query);
try {
    $stmt->execute();
}
catch(PDOException $Exception ) {
}
?>
