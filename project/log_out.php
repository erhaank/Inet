<?php
unset($_COOKIE['projekt']);
setcookie('projekt', null, -1, '/');
header("location:index.php");
?>