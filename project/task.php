<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);


// set the PDO error mode to exception
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$user_id = $_POST["user_id"];
$category = $_POST["category_name"];
$minutes = $_POST["minutes"];
$name = $_POST["task_name"];
$description = $_POST["task_description"];

if ($name == "") {
	return;
}

$query = "select id from category where name = :category and userId = :user_id";

$stmt = $db->prepare($query);

$stmt->bindParam(':category', $category);
$stmt->bindParam(':user_id', $user_id);

$stmt->execute();

$category_id = $stmt->fetch(PDO::FETCH_ASSOC)['id'];


$query = "insert into task (userid, categoryId, minutes, name, description)
	values (:user_id, :category_id, :minutes, :name, :description)";

$stmt = $db->prepare($query);

$stmt->bindParam(':user_id', $user_id);
$stmt->bindParam(':category_id', $category_id);
$stmt->bindParam(':minutes', $minutes);
$stmt->bindParam(':name', $name);
$stmt->bindParam(':description', $description);

try {
    $stmt->execute();
    echo "<p style='color:green'>Successfully added task '{$name}'</p>";
}
catch(PDOException $Exception ) {
    echo "<p style='color:red'>Couldn't add task '{$name}'</p>";
}
?>
