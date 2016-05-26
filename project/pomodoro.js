$(document).ready(function() {
	var username = $("#username").text();
	var taskInProgress = false;
	// Generate all of the dropdown choises in the forms
	$.ajax(
		{
		type: "POST",
		url: "generate_options.php",
		data: "type=category&username="+username,
		success: function(result) {
			$(".generate_cat").html(result);
		},
		error: function(a, b, c) {
			console.log(b);
		}
	});

	$.ajax(
		{
		type: "POST",
		url: "generate_options.php",
		data: "type=task&username="+username,
		success: function(result) {
			$(".generate_task").html(result);
		},
		error: function(a, b, c) {
			console.log(b);
		}
	});

	$.ajax(
		{
		type: "POST",
		url: "generate_options.php",
		data: "type=session&username="+username,
		success: function(result) {
			$(".generate_session").html(result);
		},
		error: function(a, b, c) {
			console.log(b);
		}
	});

	$.ajax(
		{
		type: "POST",
		url: "generate_workflow.php",
		data: "user_id="+username,
		success: function(result) {
			$("#workflow").html(result);
		},
		error: function(a, b, c) {
			console.log(b);
		}
	});

	$.ajax(
		{
		type: "POST",
		url: "generate_clock.php",
		data: "user_id="+username,
		success: function(result) {
			var endTime = parseInt(result.split(",")[0]);
			var description = result.split(",")[1];
			//alert("End time: "+endTime+", Description: "+description);
			if (endTime != 0) {
				taskInProgress = true;
				startTimer(endTime, $("#timer"), description);
			}
		},
		error: function(a, b, c) {
			console.log(b);
		}
	});


	// Function invoked when pressing 'Add category'
	$("#catForm").on("submit", function(e) {
		var form_params = $("#catForm").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "category.php",
			data: form_params,
			success: function(result) {
				$("#cat_form_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	// Function invoked when pressing 'Add task'
	$("#taskForm").on("submit", function(e) {
		var form_params = $("#taskForm").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "task.php",
			data: form_params,
			success: function(result) {
				$("#task_form_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	// Function invoked when pressing 'Add session'
	$("#sessionForm").on("submit", function(e) {
		var form_params = $("#sessionForm").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "session.php",
			data: form_params,
			success: function(result) {
				$("#session_form_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	// Function invoked when pressing 'Add session to task'
	$("#sessionTaskForm").on("submit", function(e) {
		var form_params = $("#sessionTaskForm").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "sessionTask.php",
			data: form_params,
			success: function(result) {
				$("#session_task_form_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	// Function invoked when pressing 'Add task to workflow'
	$("#add_task").on("submit", function(e) {
		var form_params = $("#add_task").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "add_task_to_workflow.php",
			data: form_params,
			success: function(result) {
				$("#workflow_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	// Function invoked when pressing 'Add session to workflow'
	$("#add_session").on("submit", function(e) {
		var form_params = $("#add_session").serialize();
		form_params = form_params + "&user_id="+username;
		$.ajax(
			{
			type: "POST",
			url: "add_session_to_workflow.php",
			data: form_params,
			success: function(result) {
				$("#workflow_result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
	});

	$("body").on("click", ".workflow_task", function(element){
		if (taskInProgress) {
			//Cannot add a task if another is already in progress.
			alert("Another task is already in progress...");
			return;
		}
        var flow_id = element.target.parentElement.id;
        var id = flow_id.split("_")[1];
        var duration = element.currentTarget.attributes.getNamedItem("value").value;
        var endTime = getEndTime(duration);
        $.ajax(
			{
			type: "POST",
			url: "start_working.php",
			data: "flow_id="+id+"&user_id="+username+"&end_time="+endTime,
			success: function(result) {
				if (result == "ERROR") {
					return;
				}
				element.currentTarget.className = "in_progress";
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
    });

    $("body").on("click", ".remove_task", function(element){
        var flow_id = element.target.parentElement.id;
        var id = flow_id.split("_")[1];
        $.ajax(
			{
			type: "POST",
			url: "remove_from_workflow.php",
			data: "flow_id="+id,
			success: function(result) {
				console.log(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
    });

    $("body").on("click", "#remove_in_progress", function(element){
        $.ajax(
			{
			type: "POST",
			url: "remove_from_inprogress.php",
			data: "user_id="+username,
			success: function(result) {
				console.log(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
    });

    $("body").on("click", "#log_out", function(element){
        $.ajax(
			{
			type: "POST",
			url: "log_out.php",
			data: "",
			success: function(result) {
				console.log(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
    });

    function getEndTime(duration) {
		duration = duration*60;	// from minutes to seconds
	    var endTime = Date.now()+(duration*1000);
	    return endTime;
	}

	function startTimer(endTime, display, displayText) {
	    var hours, minutes, seconds;

	    var intervalId = setInterval(function () {
	    	var milliLeft = endTime - Date.now();
	    	var secondsLeft = parseInt(milliLeft/1000,10);
	    	hours = parseInt(secondsLeft / 3600, 10);
	        minutes = parseInt((secondsLeft % 3600) / 60, 10);
	        seconds = parseInt(secondsLeft % 60, 10);

	        hours = hours < 10 ? "0" + hours : hours;
	        minutes = minutes < 10 ? "0" + minutes : minutes;
	        seconds = seconds < 10 ? "0" + seconds : seconds;

	        display.html("<button id='remove_in_progress' type='button'></button><p class='timer'>" + hours + ":" + minutes + ":" + seconds+"</p>" + "<p class='timer_text'>" + displayText + "</p>");

	        if (secondsLeft <= 0) {
	            taskFinished(intervalId);
	        }
	    }, 1000);
	}

	function taskFinished(interval) {
		clearInterval(interval);
		// TODO: Fixa pling och kanske popup? Och ta bort tasken från workspace. Och ta bort från databasen
		$.ajax(
			{
			type: "POST",
			url: "task_finished.php",
			data: "user_id="+username,
			success: function(result) {
				console.log(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		//window.location.reload();
	}
});