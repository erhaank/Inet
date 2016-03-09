<%@page import="java.util.*,bean.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><html>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
<meta charset="UTF-8">
<title>Trade 0.1 Beta</title>
</head>

<body>
<jsp:useBean class="bean.Securities" id="securities" scope="session"/>
<jsp:useBean class="bean.Trades" id="trades" scope="session"/>
<jsp:useBean class="bean.User" id="user" scope="application"/>

<jsp:setProperty name="securities" property="*"/>
<jsp:setProperty name="trades" property="*"/>
<jsp:setProperty name="user" property="*"/>

<c:choose>
  <c:when test="${empty user.name}">
<h1>Ny session!</h1>
<form action="TradeController">
<input type="hidden" name="action" value="addNewUser">
Enter full name:<input type="text" name="fullname"><br>
<input type="submit"></form>

</c:when>
<c:otherwise>

<%@page contentType="text/html;charset=UTF-8"%>

<h1>Welcome <c:out value="${user.name}"></c:out></h1>

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
<c:forEach items="${securities.getList()}" var="item"> 
    <option value="${item.name}">${item.name}</option>
</c:forEach>
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
<c:forEach items="${securities.getList()}" var="item">
    <option value="${item.name}">${item.name}</option>
</c:forEach>
</select><br>
<input type="submit" value="Utför">
</form>


<c:if test="${fn:length(trades.getList()) gt 0}">
<table>
<tr>
<th><b>Security</b></th>
<th><b>Seller</b></th>
<th><b>Buyer</b></th>
<th><b>Amount</b></th>
<th><b>Price</b></th>
<th><b>Date</b></th>
</tr>
<c:forEach items="${trades.getList()}" var="item">
	<tr>
    <td>${item.name}</td>
    <td>${item.seller}</td>
    <td>${item.buyer}</td>
    <td>${item.amount}</td>
    <td>${item.price}</td>
    <td>${item.date}</td>
    </tr>
</c:forEach>
</table>
</c:if>

</c:otherwise>
</c:choose>


</html>
