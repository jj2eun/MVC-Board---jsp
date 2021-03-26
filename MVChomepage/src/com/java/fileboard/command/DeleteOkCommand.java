package com.java.fileboard.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.board.model.BoardDao;
import com.java.command.Command;

public class DeleteOkCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String boardNumber = request.getParameter("boardNumber");
		String pageNumber = request.getParameter("pageNumber");
		String password = request.getParameter("password");
		
		int check = BoardDao.getInstance().delete(boardNumber, password);
		
		request.setAttribute("pageNumber", pageNumber);
		request.setAttribute("check", check);
		
		return "/WEB-INF/fileboard/deleteOk.jsp";
	}

}
