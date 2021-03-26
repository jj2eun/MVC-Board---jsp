package com.java.fileboard.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.board.model.BoardDao;
import com.java.board.model.BoardDto;
import com.java.command.Command;

public class UpdateCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		int boardNumber = Integer.parseInt(request.getParameter("boardNumber"));
		BoardDto boardDto = BoardDao.getInstance().updFunc(boardNumber);
		
		request.setAttribute("boardDto", boardDto);
		request.setAttribute("pageNumber", pageNumber);
		
		return "/WEB-INF/fileboard/update.jsp";
	}

}
