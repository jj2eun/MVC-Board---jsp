function boardCheck(obj){
	alert("글쓰기 OK");
}

function updFunc(root, boardNumber, pageNumber){
/* 글번호를 받아서 해당 게시글 업데이트 */
	var url = root+"/fileboard/update.do?boardNumber="+boardNumber+"&pageNumber="+pageNumber;
	location.href = url;
}

function delFunc(root, boardNumber, pageNumber){
/* 삭제버튼 */
	var url = root+"/fileboard/delete.do?boardNumber="+boardNumber+"&pageNumber="+pageNumber;
	location.href = url;
}

function replyFunc(root, boardNumber, groupNumber, sequenceNumber, sequenceLevel){
/* 답글 버튼 */
	var url = root+"/fileboard/write.do?boardNumber="+boardNumber+"&groupNumber="+groupNumber+"&sequenceNumber="+sequenceNumber+"&sequenceLevel="+sequenceLevel;
	location.href = url;
}