package com.java.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.board.model.BoardDao;
import com.java.board.model.BoardDto;
import com.java.command.Command;

public class ReadCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int boardNumber = Integer.parseInt(request.getParameter("boardNumber"));
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		
		// Dto(객체 사용 - private로 선언 된 변수)은 시큐어 코딩 중 하나
		// DB에 담긴 게시글정보를 Dto에 담는다(boardDao)
		BoardDto boardDto = BoardDao.getInstance().read(boardNumber);
		
		request.setAttribute("boardDto", boardDto);
		request.setAttribute("pageNumber", pageNumber);
		
		return "/WEB-INF/board/read.jsp";
	}

}
