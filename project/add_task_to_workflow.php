<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
$task_name = $_POST["task_name"];

//Get task id
$query = "select id from task where name = '{$task_name}' and userId = '{$user_id}'";

$stmt = $db->prepare($query);
/*$stmt->bindParam(':task_name', $task_name);
$stmt->bindParam(':user_id', $user_id);*/
$stmt->execute(); 
$task_id = $stmt->fetch(PDO::FETCH_ASSOC)['id'];

// Insert into workflow
$query = "insert into workflow(userId, taskId, amount) values ('{$user_id}', {$task_id}, 1)";

$stmt = $db->prepare($query);

try {
    $stmt->execute();
    echo "<p style='color:green'>Successfully added task to workflow'{$task_name}'</p>";
}
catch(PDOException $Exception ) {
    echo "<p style='color:red'>Couldn't add task '{$task_name}' to workflow</p>";
}
?>
