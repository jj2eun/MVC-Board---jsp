package com.java.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.command.Command;
import com.java.member.model.MemberDao;

public class DeleteOkCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		
		// db에 id와 password의 값이 일치한 경우 ResultSet 리턴해줌
		// ResultSet이 있으면 check = 1로 설정
		
		int check = 0;
		
		check = MemberDao.getInstance().deleteCheck(id,password);
		
		request.setAttribute("check", check);
		
		return "/WEB-INF/member/deleteOk.jsp";
	}

}
