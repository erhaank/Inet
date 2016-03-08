<html>

<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
<meta charset="UTF-8">
<title>Trade 0.1 Beta</title>
</head>

<body>
<jsp:useBean class="bean.Security" id="security" scope="session"/>
<jsp:useBean class="bean.Order" id="order" scope="session"/>
<jsp:useBean class="bean.Trade" id="trade" scope="session"/>

<jsp:setProperty name="security" property="*"/>
<jsp:setProperty name="order" property="*"/>
<jsp:setProperty name="trade" property="*"/>
<%@page contentType="text/html;charset=UTF-8"%>
<%
//if (request.getParameter("message") != null)
//out.println(request.getParameter("message"));
%>
<jsp:getProperty name="security" property="name"/>

<h3>Addera ett värdepapper</h3>
<form action="TradeController">
<input type="hidden" name="action" value="addSecurity">
<input type="text" name="security" value=""><br>
<input type="submit" value="Utför">
</form>

<h3>Lägg en köp/säljorder på ett värdepapper</h3>
<form action="TradeController">
<input type="hidden" name="action" value="addOrder">
Värdepapper: <select name="security">
<option value="Ericsson">Ericsson</option>
<option value="Telia">Telia</option>
<option value="Volvo">Volvo</option>
</select><br>
Köp: <input type="radio" name="buyOrSell" value="B" checked>
Sälj: <input type="radio" name="buyOrSell" value="S"><br>
Pris: <input type="number" name="price" value=""><br>
Antal: <input type="number" name="amount" value=""><br>
<input type="submit" value="Utför">
</form>

<h3>Visa avslutade affärer i ett värdepapper</h3>
<form action="TradeController">
<input type="hidden" name="action" value="viewTrades">
Värdepapper: <select name="security">
<option value="Ericsson">Ericsson</option>
<option value="Telia">Telia</option>
<option value="Volvo">Volvo</option>
</select><br>
<input type="submit" value="Utför">
</form>

</html>
