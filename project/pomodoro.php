<html>
<head>
<link rel="stylesheet" type="text/css" href="pomodoro.css">
<script src="jquery-1.12.0.min.js"></script>
<script src="pomodoro.js"></script>
</head>

<body>

<div id='test'></div>

<?php
ob_start();
if(!isset($_COOKIE["projekt"])) {
	header("location:index.php");
} else {
	$username = explode("=", $_COOKIE["projekt"])[1]; // Gets the username from the cookie_values (assumes certain things)
	echo "<p>Logged in as {$username}</p>";
	echo "<p id='username' class='hidden'>{$username}</p>";
}
?>
<button id="log_out">Log out</button>
<br>
<div class='menu'>
<p class='label_head'>Add a new category</p>

<form id="catForm" accept-charset="utf-8">
<label>Category name:</label>
<input type="text" name="category_name"><br>
<input type="submit" value="Add">
</form>
<div id='cat_form_result'></div>
<br>

<p class='label_head'>Add a new task</p>
<form id="taskForm" accept-charset="utf-8">
<label>Category:</label>
<!--<input type="text" name="category_name"><br>-->
<select class='generate_cat' name='category_name'></select><br>
<label>Minutes:</label>
<input type="number" name="minutes"><br>
<label>Name:</label>
<input type="text" name="task_name"><br>
<label>Description:</label>
<input type="text" name="task_description"><br>

<input type="submit" value="Add">
</form>
<div id='task_form_result'></div>

<br>

<p class='label_head'>Add a new session</p>
<form id="sessionForm" accept-charset="utf-8">
<label>Session name:</label>
<input type="text" name="session_name"><br>
<label>Description:</label>
<input type="text" name="session_description"><br>

<input type="submit" value="Add">
</form>
<div id='session_form_result'></div>
<br>

<p class='label_head'>Add a task to a session</p>
<form id="sessionTaskForm" accept-charset="utf-8">
<label>Task:</label>
<select name='task_name' class='generate_task'></select><br>
<label>Session name:</label>
<select name='session_name' class='generate_session'></select><br>
<label>Amount:</label>
<input type="text" name="amount"><br>
<input type="submit" value="Add">
</form>
<div id='session_task_form_result'></div>
</div>

<div id='add_tasks_div' class='menu'>
<p class='label_head'>Add single task to workflow</p>
<form id='add_task' accept-charset="utf-8">
<select class='generate_task' name='task_name'></select>
<input id='add_task_submit' class='add_workflow' type="submit" value="Add task">
</form>

<p class='label_head'>Add session to workflow</p>
<form id='add_session' accept-charset="utf-8">
<select class='generate_session' name='session_name'></select>
<input id='add_session_submit' class='add_workflow' type="submit" value="Add session">
</form>
</div>

<div id='timer'></div>

<div id='workflow' class='workflow'>
	<p id='workflow_result'></p>
</div>

</body>
</html>
