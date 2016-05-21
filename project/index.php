<?php
if(isset($_COOKIE["projekt"])) {
	// *** Commenting it out for debug reasons ***
	// Goto main page if it you are already logged in. TODO: Fix logout button in pomodoro.php
	/*if ($_COOKIE["projekt"] != "WRONG_PASSWORD" && $_COOKIE["projekt"] != "USER_EXISTS") {
		header("location:pomodoro.php");
	}*/
}
?>

<html>
<body>
<h1>Login</h1>
<?php
if(isset($_COOKIE["projekt"])) {
	if ($_COOKIE["projekt"] == "WRONG_PASSWORD") {
		echo "<p style='color:red;'>Wrong password or username</p>";
	}
}
?>
<form name="loginform" method="post" action="checklogin.php" accept-charset="utf-8">
<input name="myusername" type="text">
<input name="mypassword" type="password">
<input type="submit" value="Login">
</form>
<h2>Register</h2>
<?php
if(isset($_COOKIE["projekt"])) {
	if ($_COOKIE["projekt"] == "USER_EXISTS") {
		echo "<p style='color:red;'>Username already in use</p>";
	} else if ($_COOKIE["projekt"] == "SUCCESS") {
		echo "<p style='color:green;'>Successful register. Login above.</p>";
	}
}
?>
<form name="registerform" method="post" action="registerlogin.php">
<input name="newusername" type="text" id="newusername">
<input name="newpassword" type="password" id="newpassword">
<input type="submit" value="Register">
</form> 
</body>
</html>