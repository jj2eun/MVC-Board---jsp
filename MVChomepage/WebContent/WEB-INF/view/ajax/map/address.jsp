 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:set var="root" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a8a7eba32035c4ffdaf7906a315ec7d8"></script>
<script type="text/javascript" src="${root}/xhr/xhr.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
<script type="text/javascript">
	var arr = new Array();
	
	// 요청에 대한 처리를 하는 메소드
	function toServer(){
		var addr = document.getElementById("addr").value;
		
		var url = "https://dapi.kakao.com/v2/local/search/address.json";
		
		var params = "query=" + addr;
		
		sendRequest("GET",url,params,fromServer);
	}
	function fromServer(){
	// 완료 여부 확인
	if(xhr.readyState == 4 && xhr.status == 200){
		processJson();
	}
	}
	// 요청을 보내 반환된 데이터를 담는다
	function processJson(){
		var obj = JSON.parse(xhr.responseText);
		var y = obj.documents[0].y;
		var x = obj.documents[0].x;
		
		var mapContainer = document.getElementById('map');
		mapOption = {
				center : new kakao.maps.LatLng(y, x), //지도의 중심좌표.
				level : 5 //지도의 레벨(확대, 축소 정도)
		};
		var map = new kakao.maps.Map(mapContainer, mapOption);
	}
</script>
</head>
<body>
	<h3>주소</h3>
	<input id="addr" type="text">
	<input type="button" value="주소검색" onclick="toServer()">
	
	<div id="map" style="width: 100%; height: 400px;"></div>
</body>
</html>