<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);


// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];

$query = "select workflowId from inProgress where userId = '{$user_id}'";

$stmt = $db->prepare($query);
$stmt->execute();
$workflow_id = $stmt->fetch(PDO::FETCH_ASSOC)['workflowId'];

$delInProg = "delete from inProgress where userId = '{$user_id}'";
$delWorkflow = "delete from workflow where id = {$workflow_id}";

$stmt = $db->prepare($delInProg);
$stmt->execute();
$stmt = $db->prepare($delWorkflow);
$stmt->execute();
?>
