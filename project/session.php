<?php 

$user="agnesam_admin";
$password="FfXD1Ehl";
$db = new PDO('mysql:host=mysql-vt2016.csc.kth.se;dbname=agnesam;charset=utf8', $user, $password);

$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);


$user_id = $_POST["user_id"];
$name = $_POST["session_name"];
$description = $_POST["session_description"];

if ($name == "") {
	return;
}

$query = "insert into session (userid, name, description)
	values (:user_id, :name, :description)";

$stmt = $db->prepare($query);

$stmt->bindParam(':user_id', $user_id);
$stmt->bindParam(':name', $name);
$stmt->bindParam(':description', $description);

try {
    $stmt->execute();
    echo "<p style='color:green'>Successfully added session '{$name}'</p>";
}
catch(PDOException $Exception ) {
    echo "<p style='color:red'>Couldn't add session '{$name}'</p>";
}
?>
