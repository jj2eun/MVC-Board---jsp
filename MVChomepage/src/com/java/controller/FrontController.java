package com.java.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.command.Command;

/**
 * Servlet implementation class FrontController
 */
@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static final Logger logger = Logger.getLogger(FrontController.class.getName());
	public static final String logMsg= "logMsg~~~~~~~~~~~~~~~~~";
	
	private HashMap<String, Object> commandmap = new HashMap<String, Object>();
	
	public FrontController() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	public void init(ServletConfig config) throws ServletException{
		// init-param에 저장된 configFile을 가져옴
		String configFile = config.getInitParameter("configFile");
		
		// config파일의 실제 물리적인 경로 획득
		String path = config.getServletContext().getRealPath(configFile);
		
		logger.info(logMsg + path); 
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		Properties pro = new Properties();
		
		try {
			fis = new FileInputStream(path); // 물리적인 파일을 다운받음
			bis = new BufferedInputStream(fis, 1024); // 파일을 버퍼단위로 쪼갬
			pro.load(bis); // Properties객체에 정보를 버퍼단위로 담음
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null) {
					fis.close();	// close
				}
				if(bis!=null) {
					bis.close();	// close
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// 자바의 컬렉션에 저장되어있는 요소를 읽어오는 표준 인터페이스(Iterator)
		Iterator<Object> keyIter = pro.keySet().iterator();
		
		while(keyIter.hasNext()) {
			String command = (String) keyIter.next();	// ex) /member/main.do
			String className = pro.getProperty(command);	// ex) com.java.member.command.MainCommand
			logger.info(command + "\t" + className);
			
			try {									 // ex) className : java.com.member.command.RegisterCommand
				Class<?> handlerClass = Class.forName(className); // 의미상의 객체 생성 (객체에 대한 정보를 담는 handlerClass생성)
				// 정보를 이용하여 new를 통한 객체 생성
				Object handlerInstance = handlerClass.getDeclaredConstructor().newInstance(); 	// 인스턴스 부여
				
				// getDeclaredConstructor : handlerClass(java.com.member.command.RegisterCommand)경로에 파일이 실제 존재한다면 해당 객체를 반환(newInstnace)해준다
				
				commandmap.put(command, handlerInstance);	// 주소와 객체 commandmap에 담는다
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getServletPath();	// ex) cmd: /member/register.do (url-pattern 반환)
		logger.info(cmd);
		
		String viewPage = null; 
		try {
			Command com = (Command) commandmap.get(cmd);
			// commandmap에서 cmd(/member/register.do)를 이용해 get한다 -> 객체(java.com.member.command.RegisterCommand)를 가져온다
			
			viewPage = com.proRequest(request, response);	// RegisterCommand객체의 proRequest 메소드 실행
			// viewPage = "/WEB-INF/member/register.jsp"
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(viewPage!=null) {
			RequestDispatcher rd = request.getRequestDispatcher(viewPage);	// RequestDispathcer을 통해 viewPage실행
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
