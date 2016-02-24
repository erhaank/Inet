$(document).ready(function() {
	$("#result").hide();
	var searchParams;
    var accending = false;
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

	$("#result").show();
	return false;
	});

    $("body").on("click", ".sort", function(){
        if(searchParams == null) {
            searchParams = $("#searchparams").text();
        }
        if(accending) {
            accending = false;
            acc = "DESC";

        } else {
            accending = true;
            acc = "";
        }
        $.ajax(
            {
            type: "POST",
            url: "result.php",
            data: searchParams + "&order_variable=" + $(this).attr('title') + " " + acc,
            success: function(result) {
                $("#result").html(result);
            },
            error: function(a, b, c) {
                console.log(b);
            }
        });
    });
	
});
