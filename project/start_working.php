<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$flow_id = $_POST["flow_id"];
$user_id = $_POST["user_id"];
$end_time = $_POST["end_time"];

//Try inserting into inProgress
$query = "insert into inProgress values(:user, :flow, :end_time)";

$stmt = $db->prepare($query);
$stmt->bindParam(':user', $user_id);
$stmt->bindParam(':flow', $flow_id);
$stmt->bindParam(':end_time', $end_time);

try {
	$stmt->execute();
}
catch(PDOException $Exception ) {
	echo "ERROR";
    return;
}
?>
