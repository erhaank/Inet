<?php
if(isset($_COOKIE["projekt"])) {
	// Goto main page if it you are already logged in.
	if (strcmp(substr($_COOKIE["projekt"], 0, 9), "username=") == 0) {
		header("location:pomodoro.php");
	}
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
<label>Username</label>
<input name="myusername" type="text">
<label>Password</label>
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
<label>Username</label>
<input name="newusername" type="text" id="newusername">
<label>Password</label>
<input name="newpassword" type="password" id="newpassword">
<input type="submit" value="Register">
</form> 
</body>
</html>