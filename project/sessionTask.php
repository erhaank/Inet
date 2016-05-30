<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
$task_name = $_POST["task_name"];
$session_name = $_POST["session_name"];
$amount = $_POST["amount"];

if ($task_name == "") {
	return;
}

//Get task id
$query = "select id from task where name = :task_name and userId = :user_id";

$stmt = $db->prepare($query);
$stmt->bindParam(':task_name', $task_name);
$stmt->bindParam(':user_id', $user_id);
$stmt->execute(); 
$task_id = $stmt->fetch(PDO::FETCH_ASSOC)['id'];

//Get session id
$query = "select id from session where name = :session_name and userId = :user_id";

$stmt = $db->prepare($query);
$stmt->bindParam(':session_name', $session_name);
$stmt->bindParam(':user_id', $user_id);
$stmt->execute(); 
$session_id = $stmt->fetch(PDO::FETCH_ASSOC)['id'];

//Add sessionTask
$query = "insert into sessionTasks (taskid, sessionid, amount)
	values (:task_id, :session_id, :amount)";

$stmt = $db->prepare($query);
$stmt->bindParam(':task_id', $task_id);
$stmt->bindParam(':session_id', $session_id);
$stmt->bindParam(':amount', $amount);

try {
    $stmt->execute();
    echo "<p style='color:green'>Successfully added task to session</p>";
}
catch(PDOException $Exception ) {
    echo "<p style='color:red'>Couldn't add task to session</p>";
}
?>
