$(document).ready(function() {
	var searchParams;
	$("#myForm").on("submit", function(e) {
		searchParams = $("#myForm").serialize();
		$.ajax(
			{
			type: "POST",
			url: "result.php",
			data: searchParams,
			success: function(result) {
				$("#result").html(result);
			},
			error: function(a, b, c) {
				console.log(b);
			}
		});
	return false;
	});
	
});
