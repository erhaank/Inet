<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$flow_id = $_POST["flow_id"];

//Get the workflow
$query = "select amount from workflow where id = {$flow_id}";

$stmt = $db->prepare($query);
$stmt->execute();
$results = $stmt->fetchAll();
//print_r($results);
$res = $results[0];

$amount = $res['amount'];
if ($amount == 1) {	//Delete
	$query = "delete from workflow where id = {$flow_id}";
	
} else {
	$new_amount = $amount - 1;
	$query = "update workflow set amount = {$new_amount} where id = {$flow_id}";
}
$stmt = $db->prepare($query);
$stmt->execute();
?>
