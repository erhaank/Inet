$(document).ready(function() {
	var username = $("#username").text();
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
        var flow_id = element.target.parentElement.id;
        //Start the specified task. 
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
				//$("#debug").html(result);
				console.log(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
		window.location.reload();	//Reload the page so that the newly added component appears.
		return false;
    });


});