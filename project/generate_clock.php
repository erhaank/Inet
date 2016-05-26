<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);


$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$user_id = $_POST["user_id"];

//Get the task params
$query = "select endTime, description from workflow, task, inProgress where workflow.id = inProgress.workflowId and workflow.taskId = task.id and inProgress.userId = :user";

$stmt = $db->prepare($query);
$stmt->bindParam(':user', $user_id);
try {
	$stmt->execute();
} catch(PDOException $Exception ) {
	echo "0,"+$query;
    return;
}

$results = $stmt->fetchAll();

if (count($results) == 0) { // If empty => none is currently in progress
	echo "0,";
	return;
}

$res = $results[0]; // Should only be one result

echo "{$res['endTime']},{$res['description']}"; // TODO maybe get a better description using name and category?

?>
