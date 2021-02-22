<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>
<h3>DB Test11</h3>
<table border="1">
<c:forEach var="member" items="${memberList}" varStatus="stat">
<tr><td>${member.id}<td><td>${member.pw}<td></tr>
</c:forEach>
</table>
</body>
</html>