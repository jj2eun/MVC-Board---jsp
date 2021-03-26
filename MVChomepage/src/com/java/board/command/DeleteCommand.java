package com.java.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.command.Command;

public class DeleteCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String boardNumber = request.getParameter("boardNumber");
		String pageNumber = request.getParameter("pageNumber");
		
		request.setAttribute("boardNumber", boardNumber);
		request.setAttribute("pageNumber", pageNumber);
		
		return "/WEB-INF/board/delete.jsp";
	}

}
