<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>kakaoMap</title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a8a7eba32035c4ffdaf7906a315ec7d8"></script>
</head>
<body>
	<div>
		<h3>카카오 지도</h3>
	</div>
	<div id="map" style="width:500px;height:400px;"></div>
	<script type="text/javascript">
		var container = document.getElementById('map');
		// 지도를 생성 할 변수 만들자
		var options = { //지도를 생성할 때 필요한 기본 옵션
			center : new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
			level : 3 //지도의 레벨(확대, 축소 정도)
		};

		var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
	</script>
</body>
</html>