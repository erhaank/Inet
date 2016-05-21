<?php

$tbl_name="user"; // Table name 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

// username and password sent from form 
$myusername = $_POST["myusername"]; 
$mypassword = $_POST["mypassword"];

$sql="select * from {$tbl_name} where id='{$myusername}' and password={$mypassword}";

$stmt = $db->prepare($sql);
$stmt->execute();

$count=$stmt->rowCount();

// If result matched $myusername and $mypassword, table row must be 1 row
if($count==1){
// Register $myusername, $mypassword and redirect to file "login_success.php"
$cookie_name = "projekt";
$cookie_value = "username=".$myusername; //If we want to add more values, add &<key>=<value> etc
setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/");
header("location:pomodoro.php");
}
else {
$cookie_name = "projekt";
$cookie_value = "WRONG_PASSWORD";
setcookie($cookie_name, $cookie_value, time() + (86400 * 30), "/");
header("location:index.php");
}
?>