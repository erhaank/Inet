<?php
//phpinfo();
session_start();

class Guess{
  var $secretNumber;
  var $numberOfGuesses;
  var $guess;
  var $guessL=-1;
  var $guessH=101;

  function Guess(){
    $this->reset();        
  }

  function __construct(){
    $this->reset();        
  }

  function reset(){
    srand(time());
    $this->secretNumber = (rand()%100);
    $this->numberOfGuesses = 0;
    $this->guessL=-1;
    $this->guessH=101;
  }

  function setGuess($var){
	if($var<$this->guessH && $var>$this->guessL){
		$this->guess=$var;
		$this->numberOfGuesses++;
		if($var>$this->secretNumber)
			$this->guessH=$var;
		else if($var<$this->secretNumber)
			$this->guessL=$var;	
	}
  }

  function success(){
    if($this->secretNumber==$this->guess){
      return "You made it!!!";
    }else
	if($this->guessL!=-1 && $this->guessH!=101)
	      return "Nope, guess a number between $this->guessL and $this->guessH <br>";
    else if($this->secretNumber<$this->guess)
      return "Nope, guess lower<br>";
    else if($this->secretNumber>$this->guess)
      return "Nope, guess higher<br>";
  }
}
?>

<html>
<head><title>Number Guess Game</title>
<script type = "text/javascript">
  function inputfocus(form){
  form.guess.focus()
}

function myFunction() {
    alert("Hello\nHow are you?");
}
</script>

</head>

<body onLoad="inputfocus(document.guessform);"> 


<?php
//  if(!session_is_registered('guessObject')){
if(!isset($_SESSION['guessObject'])){
  $_SESSION['guessObject'] = new Guess();
//    $_SESSION['guessObject']->secretNumber=rand()%100;
//    print "secret number".$_SESSION['guessObject']->secretNumber;
    print "Welcome to the Number Guess Game. Guess a number between 1 and 100.";

  }
  else{
    $_SESSION['guessObject']->setGuess($_GET['guess']);
    print $_SESSION['guessObject']->success();
    print "You have made {$_SESSION['guessObject']->numberOfGuesses} guess(es)<br>";

  }
?>


<form name="guessform">
<input type=text name=guess>
<input type=submit  value="Guess">
</form>

</body></html>
