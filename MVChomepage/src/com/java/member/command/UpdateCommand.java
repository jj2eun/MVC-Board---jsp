package com.java.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.command.Command;
import com.java.member.model.MemberDao;
import com.java.member.model.MemberDto;

public class UpdateCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 회원정보 출력되게 -> Dao를 통해 회원정보 가져오기
		// request에 memberDto담기
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		System.out.println("session?:" + id);
		
		MemberDto memberdto = MemberDao.getInstance().getUserInfo(id);
		
		request.setAttribute("memberDto", memberdto);
		
		return "/WEB-INF/member/update.jsp";
	}

}
