<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${root}/board/deleteOk.do" method="post">	
		<div align="center">
			<h2>비밀번호를 입력하세요</h2>
			<input type="password" name="password">
			<input type="hidden" name="boardNumber" value="${boardNumber}">
			<input type="hidden" name="pageNumber" value="${pageNumber}">
			<input type="submit" value="확인">
		</div>
	</form>
</body>
</html>