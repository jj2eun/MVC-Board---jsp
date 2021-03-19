<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="../../index.jsp"/>
	<br><br>
	<!-- ${id} : loginOk.jsp에서 설정한 session(id) -->
	<h3>안녕하세요 ${id}님 즐거운 시간 되세요</h3>
</body>
</html>