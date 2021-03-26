package com.java.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.board.model.BoardDao;
import com.java.command.Command;

public class UpdateOkCommand implements Command{
	
	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		// 게시글 업데이트(제목, 메일, 내용, 비번)
		String boardNumber = request.getParameter("boardNumber");
		String pageNumber = request.getParameter("pageNumber");
		String subject = request.getParameter("subject");
		String email = request.getParameter("email");
		String content = request.getParameter("content");
		String password = request.getParameter("password");
		
		int check = BoardDao.getInstance().update(boardNumber,subject,email,content,password);
		
		request.setAttribute("check", check);
		request.setAttribute("pageNumber", pageNumber);
		
		return "/WEB-INF/board/updateOk.jsp";
	}

}
