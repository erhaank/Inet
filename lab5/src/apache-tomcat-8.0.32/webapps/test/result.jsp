<html>
<body>
<h1 align="center"> Results </h1>
<%
String quote = (String) request.getAttribute("quote");
%>
<center>
<p>
<% out.print(quote); %>
</p>
<form action="quote.html">
<input type="submit" value="Again?">
</form>
</center>
</body>
</html>