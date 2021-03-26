<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<c:set var="root" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${root}/css/board/boardStyle.css">
<script type="text/javascript" src="${root}/javaScript/board/board.js"></script>
</head>
<body>
	<form action="${root}/board/updateOk.do" method="post">
	<div id="createform">
		<input type="hidden" name="boardNumber" value="${boardDto.boardNumber}">
		<input type="hidden" name="pageNumber" value="${pageNumber}">
		<div class="menu" style="border-bottom-width: 0px;">
			<div id="id">작성자</div>
			${boardDto.writer}	
		</div>
		<div class="menu" style="border-bottom-width: 0px;">
			<div id="id">제목</div>
			<input type="text" value="${boardDto.subject}" name="subject">
		</div>
		<div class="menu" style="border-bottom-width: 0px;">
			<div id="id">이메일</div>
			<input type="text" value="${boardDto.email}" name="email">
		</div>
		<div class="content" style="border-bottom-width: 0px;">
			<div id="text">내용</div>
			<textarea name="content" rows="12" cols="65">${boardDto.content}</textarea>
		</div>
		<div class="menu" style="border-bottom-width: 0px;">
			<div id="id">비밀번호</div>
			<input type="password" name="password" size="30" value="${boardDto.password}">
		</div>
		<div class="menu" style="border-bottom-width: 3px; text-align: center;">
			<input type="submit" value="수정 완료">
			<input type="reset" value="다시작성"> <%-- url이 완성될 수 있도록 경로에 대한 정보를 매개변수로 받아야 한다. 글 번호, 페이지 번호--%>
			<input type="button" value="글 목록" onclick="location.href='${root}/board/list.do?pageNumber=${pageNumber}'">
		</div>
	</div>
	</form>
</body>
</html>