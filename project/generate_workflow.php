<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
//Get the workflow
$query = "select id, taskId from workflow where userId = :user_id";

$stmt = $db->prepare($query);
$stmt->bindParam(':user_id', $user_id);
$stmt->execute(); 
$results = $stmt->fetchAll();
//print_r($results);

// Get the id of the task in progress
$query = "select workflowId from inProgress where userId = :user_id";

$stmt = $db->prepare($query);
$stmt->bindParam(':user_id', $user_id);
$stmt->execute(); 
$in_progress = $stmt->fetchAll()[0][0];

foreach ($results as $res) {
	$task_id = $res['taskId'];
	$id = $res['id'];
	$query = "select category, minutes, name, description from task where id = :task_id";

	$stmt = $db->prepare($query);
	$stmt->bindParam(':task_id', $task_id);
	$stmt->execute(); 
	$params = $stmt->fetchAll();
	foreach ($params as $param) {
		if ($id == $in_progress) {
			echo "<div class='in_progress' id='workflow_{$id}' value='{$param['minutes']}'><button class='remove_task' type='button'></button><p>{$param['name']}</p><p>{$param['category']}</p><p>{$param['description']}</p><p>Time: {$param['minutes']} minutes</p></div>";

		} else { 
			echo "<div class='workflow_task' id='workflow_{$id}' value='{$param['minutes']}'><button class='remove_task' type='button'></button><p>{$param['name']}</p><p>{$param['category']}</p><p>{$param['description']}</p><p>Time: {$param['minutes']} minutes</p></div>";
		}
	}
}
?>
