<html>
<head>
<!-- <link rel="stylesheet" type="text/css" href="pomodoro.css"> -->
<!-- <script src="jquery-1.12.0.min.js"></script>
<script src="pomodoro.js"></script> -->
</head>

<body>
<form id="catForm" action="category.php" method="post" accept-charset="utf-8">
<label>User:</label>
<input type="text" name="user_id"><br>
<label>Category name:</label>
<input type="text" name="category_name"><br>

<input type="submit" value="Add">
</form>
<br>

<form id="taskForm" action="task.php" method="post" accept-charset="utf-8">
<label>User:</label>
<input type="text" name="user_id"><br>
<label>Category:</label>
<input type="text" name="category_name"><br>
<label>Minutes:</label>
<input type="number" name="minutes"><br>
<label>Name:</label>
<input type="text" name="task_name"><br>
<label>Description:</label>
<input type="text" name="task_description"><br>

<input type="submit" value="Add">
</form>
<br>

<form id="sessionForm" action="session.php" method="post" accept-charset="utf-8">
<label>User:</label>
<input type="text" name="user_id"><br>
<label>Session name:</label>
<input type="text" name="session_name"><br>
<label>Description:</label>
<input type="text" name="session_description"><br>

<input type="submit" value="Add">
</form>
<br>

<form id="sessionTaskForm" action="sessionTask.php" method="post" accept-charset="utf-8">
<label>Task:</label>
<input type="text" name="task_name"><br>
<label>Session name:</label>
<input type="text" name="session_name"><br>
<label>Amount:</label>
<input type="text" name="amount"><br>

<input type="submit" value="Add">
</form>

</body>
</html>




