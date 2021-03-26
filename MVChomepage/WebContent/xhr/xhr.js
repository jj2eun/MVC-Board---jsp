/**
 * 
 */
function createXHR(){
	if(window.XMLHttpRequest){
		return new XMLHttpRequest;
	} else {
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

var xhr = null;
var arr = new Array();

function sendRequest(method, url, params, callback){
	// callback함수 : 파라미터로 함수를 전달해 해당 함수 내부에서 실행되는 함수
	
	var httpMethod = method.toUpperCase();
	var httpUrl = url;
	var httpParams = (params==null || params=="")? null : params;
	
	if(httpMethod == "GET" && httpParams != null){
		httpUrl += "?"+httpParams;
	}
	arr.push(httpMethod+","+httpUrl+","+httpParams);
	
	xhr = createXHR();
	xhr.open(httpMethod, httpUrl, true);
	
	// post일 수도 있기에 post일때는
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	
	// 카카오맵api사용을 위한 헤더 설정
	xhr.setRequestHeader("Authorization", "KakaoAK 20205e11498e687e1be1cd4f6ec043e6");
	
	xhr.send(httpMethod=="POST"? httpParams : null);
	
	xhr.onreadystatechange = callback;
}