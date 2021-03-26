<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:set var="root" value="${pageContext.request.contextPath}"/>
	<c:if test="${check > 0 }">
		<script type="text/javascript">
			alert("글 수정 완료");
			location.href="${root}/fileboard/list.do?pageNumber=${pageNumber}";
		</script>
	</c:if>
	<c:if test="${check == 0 }">
		<script type="text/javascript">
			alert("글 수정 미완료");
			location.href="${root}/fileboard/read.do?boardNumber=${boardNumber}&pageNumber=${pageNumber}";
		</script>
	</c:if>
</body>
</html>