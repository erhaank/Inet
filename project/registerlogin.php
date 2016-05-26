<?php
$tbl_name="user"; // Table name


$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

// username and password sent from form 
$newusername=$_POST['newusername'];
$newpassword=$_POST['newpassword'];

$sql="insert into {$tbl_name} values('{$newusername}', {$newpassword})";

$stmt = $db->prepare($sql);
try {
    $stmt->execute();
    $cookie_name = "projekt";
	$cookie_value = "SUCCESS";
	setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/");
	header("location:index.php");
}
catch(PDOException $Exception ) {
    $cookie_name = "projekt";
	$cookie_value = "USER_EXISTS";
	setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/");
	header("location:index.php");
}
?>