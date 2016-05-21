<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
$session_name = $_POST["session_name"];

//Get session id
$query = "select id from session where name = :session_name and userId = :user_id";

$stmt = $db->prepare($query);
$stmt->bindParam(':session_name', $session_name);
$stmt->bindParam(':user_id', $user_id);
$stmt->execute(); 
$session_id = $stmt->fetch(PDO::FETCH_ASSOC)['id'];
// Get the tasks of the session
$query = "select taskId, amount from session, sessionTasks where session.id = {$session_id}";
$stmt = $db->prepare($query);
$stmt->execute();
$rows = $stmt->fetchAll();

foreach($rows as $row) {
	$task_id = $row['taskId'];
	$amount = $row['amount'];
	$query = "insert into workflow(userId, taskId, amount) values ('{$user_id}', {$task_id}, {$amount})";
	$stmt = $db->prepare($query);
	/*$stmt->bindParam(':user_id', $user_id);
	$stmt->bindParam(':task_name', $row['taskId']);
	$stmt->bindParam(':amount', $row['amount']);*/

	try {
	    $stmt->execute();
	    echo "<p style='color:green'>Successfully added session to workflow'{$session_name}'</p>";
	}
	catch(PDOException $Exception ) {
	    echo "<p style='color:red'>Couldn't add session '{$session_name} to workflow'</p>";
	}
}
?>
