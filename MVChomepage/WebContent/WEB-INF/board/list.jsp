<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>목록 페이지</title>
</head>
<body>
	<div align="right">
		<table>
			<tr>
				<td>
					<a href="${root}/board/write.do">글쓰기</a>
				</td>
			</tr>
		</table>
	</div>
	
	<c:if test="${count == 0 && boardList.size() == 0}">
		<div align="center">게시판에 저장된 글이 없습니다</div>
	</c:if>
	
	<c:if test="${count > 0}">
		<div align="center">
			<table border="1">
				<tr>
					<td align="center" width="50">번호</td>
					<td align="center" width="250">제목</td>
					<td align="center" width="70">작성자</td>
					<td align="center" width="150">작성일</td>
					<td align="center" width="50">조회수</td>				
				</tr>
				<c:forEach var="boardDto" items="${boardList}">
					<tr>
						<td align="center" width="50">${boardDto.boardNumber}</td>
						<td align="center" width="250">
							<a href="${root}/board/read.do?boardNumber=${boardDto.boardNumber}&pageNumber=${currentPage}">${boardDto.subject}</a>
						</td>
						<td align="center" width="70">${boardDto.writer}</td>
						<td align="center" width="150">
							<fmt:formatDate value="${boardDto.writeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td align="center" width="50">${boardDto.readCount}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
	
	<div align="center">
	<!-- 
		1. 페이지당 게시물 수 - 10개
		2. 총 페이지 수 - 10페이지 -> 전체 100페이지/ 10 = 10페이지
								  전체 101페이지/10 = 11페이지
								  
		첫 게시글 번호 = (요청 페이지 번호 - 1) * 페이지당 게시글 수 + 1
		마지막 게시글 번호 = 요청페이지 번호 * 페이지당 게시글 수
		첫 페이지 번호 = 1	
		마지막 페이지 번호 = 전체 게시글 수 / 페이지당 게시글 수 + (전체 게시글 수 % 페이지당 게시글 수 == 0? 0 : 1)	 
	 -->
	 
	 <fmt:parseNumber var="pageCount" value="${count/boardSize+(count%boardSize == 0? 0 : 1)}" integerOnly="true"></fmt:parseNumber>
	 <c:set var="pageBlock" value="${3}"></c:set>
	 <%-- 페이지 번호가 몇 개 까지만 보여야 할까?  --%>
	 <fmt:parseNumber var="result" value="${(currentPage-1)/pageBlock}" integerOnly="true"></fmt:parseNumber>
	 <c:set var="startPage" value="${result * pageBlock + 1}"></c:set>
	 <c:set var="endPage" value="${startPage + pageBlock -1 }"></c:set>
	 
	 <c:if test="${endPage > pageCount}">
	 	<c:set var="endPage" value="${pageCount }"></c:set>
	 </c:if>
	 <%-- 이전버튼 만들기(페이지 번호의 최초 영역 이후 부터 생성) --%>
	 <c:if test="${startPage > pageBlock}">
	 	<a href="${root }/board/list.do?pageNumber=${startPage-pageBlock}">이전</a>
	 </c:if>
	 
	 <c:forEach var="i" begin="${startPage}" end="${endPage}">
	 	<a href="${root }/board/list.do?pageNumber=${i}">[${i}]</a>
	 </c:forEach>
	 										
	 <c:if test="${endPage < pageCount}">
	 	<a href="${root}/board/list.do?pageNumber=${startPage+pageBlock}">다음</a>
	 </c:if>
	 
	 
	</div>
</body>
</html>