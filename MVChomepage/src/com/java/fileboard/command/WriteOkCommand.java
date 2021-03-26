package com.java.fileboard.command;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.java.board.model.BoardDao;
import com.java.board.model.BoardDto;
import com.java.command.Command;

public class WriteOkCommand implements Command{

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/*
		 * request.setCharacterEncoding("utf-8");
		 * 
		 * BoardDto boardDto = new BoardDto();
		 * 
		 * System.out.println(Integer.parseInt(request.getParameter("boardNumber")));
		 * System.out.println(Integer.parseInt(request.getParameter("groupNumber")));
		 * System.out.println(Integer.parseInt(request.getParameter("sequenceNumber")));
		 * System.out.println(Integer.parseInt(request.getParameter("sequenceLevel")));
		 * 
		 * int check = 0;
		 * 
		 * boardDto.setBoardNumber(Integer.parseInt(request.getParameter("boardNumber"))
		 * );
		 * boardDto.setGroupNumber(Integer.parseInt(request.getParameter("groupNumber"))
		 * ); boardDto.setSequenceNumber(Integer.parseInt(request.getParameter(
		 * "sequenceNumber")));
		 * boardDto.setSequenceLevel(Integer.parseInt(request.getParameter(
		 * "sequenceLevel")));
		 * 
		 * boardDto.setWriter(request.getParameter("name"));
		 * boardDto.setSubject(request.getParameter("subject"));
		 * boardDto.setEmail(request.getParameter("email"));
		 * boardDto.setContent(request.getParameter("content"));
		 * boardDto.setPassword(request.getParameter("password"));
		 * boardDto.setWriteDate(new Date()); 
		 * check = BoardDao.getInstance().insert(boardDto);
		 * 
		 * 
		 * request.setAttribute("check", check);
		 */
		
		// 파일 보관 객체
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 요청 처리 객체
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 요청을 리스트에 나눠서 담아라!
		List<FileItem> list = upload.parseRequest(request);
		// 나눠담은 리스트를 해석해라!
		Iterator<FileItem> iter = list.iterator();
		
		BoardDto boardDto = new BoardDto();
		HashMap<String, String> dataMap = new HashMap<String, String>();
		
		while(iter.hasNext()) {
			FileItem fileItem = iter.next();
			
			// 파일은 헤더에 숨겨서 보내는 방법밖에 안된다
			// 파일 데이터 인지, 그 외의 일반 form데이터 인지 확인
			if(fileItem.isFormField()) {
				// 일반 form 태그 데이터
				String name = fileItem.getFieldName(); // 프로퍼티
				String value = fileItem.getString("utf-8");	// 값
			
				dataMap.put(name, value);	// form tag의 데이터가 dataMap에 담겨있다
			} else {
				// 파일 데이터 
				if (fileItem.getFieldName().equals("file")) {
					if(fileItem.getName() == null || fileItem.getName().equals("")) {
						continue;
					}
					// 업로드 허용 용량 : 10mb
					upload.setFileSizeMax(1024*1024*10); // byte , kb, mb, gb
					String fileName = System.currentTimeMillis() + "_" + fileItem.getName();
					String dir = "C:\\Users\\kitri\\git\\MVCBoard-jsp\\MVChomepage\\pds";
					File file = new File(dir,fileName);
					
					BufferedInputStream bis = null;
					BufferedOutputStream bos = null;
					
					try {
						bis = new BufferedInputStream(fileItem.getInputStream(), 1024);
						bos = new BufferedOutputStream(new FileOutputStream(file), 1024);
						
						while(true) {
							int data = bis.read();
							
							if(data == -1) {
								break;
							}
							bos.write(data);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if(bis != null) bis.close();
						if(bos != null) bos.close();
					}
					boardDto.setFileName(fileName);
					boardDto.setFileSize(fileItem.getSize());
					boardDto.setPath(file.getAbsolutePath()); // hetAbsolutePath : 경로 반환 함수
				}
			}
		}
		boardDto.setWriteDate(new Date());
		
		// write.jsp의 enctype="multipart/form-data"로 인해 request.getParameter로 값을 얻어올 수 없다
		boardDto.setBoardNumber(Integer.parseInt(dataMap.get("boardNumber")));
		boardDto.setGroupNumber(Integer.parseInt(dataMap.get("groupNumber")));
		boardDto.setSequenceNumber(Integer.parseInt(dataMap.get("sequenceNumber")));
		boardDto.setSequenceLevel(Integer.parseInt(dataMap.get("sequenceLevel")));
		
		boardDto.setWriter(dataMap.get("name"));
		boardDto.setSubject(dataMap.get("subject"));
		boardDto.setEmail(dataMap.get("email"));
		boardDto.setContent(dataMap.get("content"));
		boardDto.setPassword(dataMap.get("password"));
		
		int check = BoardDao.getInstance().insert(boardDto);
		request.setAttribute("check", check);
		
		return "/WEB-INF/fileboard/writeOk.jsp";
	}
	
}
