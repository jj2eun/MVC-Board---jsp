package com.java.member.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java.database.ConnectionProvider;
import com.java.database.JDBCUtil;

public class MemberDao {
	// Data Access Object
	// Singleton Pattern : 단 한개의 객체만을 가지고 구현 한다
	// 어플리케이션에서 어떠한 클래스가 단 한 번만 메모리를 할당해 그 메모리 내에서
	// 객체를 만들어 사용하는 방법

	// Singletone Pattenr적용 . instance는 상수로 객체를 계속 new로 생성해도 값이 변하지 않는다
	private static MemberDao instance = new MemberDao(); 
			// A a = new A()
	public static MemberDao getInstance() {
		return instance;
	}
	
	public int insert(MemberDto memberDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;
			try {
				String sql = "insert into member values(member_num_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
				
				conn = ConnectionProvider.getConnection();
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, memberDto.getId());
				pstmt.setString(2, memberDto.getPassword());
				pstmt.setString(3, memberDto.getName());
				pstmt.setString(4, memberDto.getJumin1());
				pstmt.setString(5, memberDto.getJumin2());
				pstmt.setString(6, memberDto.getEmail());
				pstmt.setString(7, memberDto.getZipcode());
				pstmt.setString(8, memberDto.getAddress());
				pstmt.setString(9, memberDto.getJob());
				pstmt.setString(10, memberDto.getMailing());
				pstmt.setString(11, memberDto.getInterest());
				pstmt.setString(12, memberDto.getMember_level());
				
				value = pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.close(pstmt);
				JDBCUtil.close(conn);
			}
			
		return value;
	}

	public int idCheck(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int value = 0;
		try {
			String sql = "Select id from member where id = ?";
			
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			// ?에 해당하는 값 넣어주기
			pstmt.setString(1, id);
			
			pstmt.executeUpdate();
			
				// sql문 db로 전달
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// ID가 존재할 경우
				value = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		return value;
	}

	public String loginCheck(String id, String password) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String value = null;
		
		try {
			String sql = "select member_level from member where id = ? and password = ?";
			
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, password);
		
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				value = rs.getString("member_level");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		
		return value;
	}

	public int deleteCheck(String id, String password) {
		// 파라미터로받은 id와 password를 이용해 DB에 일치하는 값이 있는지 확인
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int value = 0;
		boolean ok = false;
		String sql = null;
		try {
			sql = "select id from member where id = ? and password = ?";
			
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			
			pstmt.executeUpdate();
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ok = true;
			}
			
			// 회원 삭제하기
			if(ok == true) {
				sql = "delete from member where id = ? and password = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, id);
				pstmt.setString(2, password);
				
				value = pstmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		
		return value;
	}

	public MemberDto getUserInfo(String id) {
		//회원정보 얻기
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberDto memberdto = new MemberDto();
		
		try {
			String sql = "select * from member where id = ?";
			
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				memberdto.setId(rs.getString("id"));
				memberdto.setPassword(rs.getString("password"));
				memberdto.setName(rs.getString("name"));
				memberdto.setJumin1(rs.getString("jumin1"));
				memberdto.setJumin2(rs.getString("jumin2"));
				memberdto.setEmail(rs.getString("email"));
				memberdto.setZipcode(rs.getString("zipcode"));
				memberdto.setAddress(rs.getString("address"));
				memberdto.setJob(rs.getString("job"));
				memberdto.setMailing(rs.getString("mailing"));
				memberdto.setInterest(rs.getString("interest"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		return memberdto;
	}

	public int Update(MemberDto memberDto) {
		int value = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			sql = "update member set password = ?, email = ?, zipcode = ?, address = ?, job = ?, mailing = ?, interest =? where id = ?";

			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memberDto.getPassword());
			pstmt.setString(2, memberDto.getEmail());
			pstmt.setString(3, memberDto.getZipcode());
			pstmt.setString(4, memberDto.getAddress());
			pstmt.setString(5, memberDto.getJob());
			pstmt.setString(6, memberDto.getMailing());
			pstmt.setString(7, memberDto.getInterest());
			pstmt.setString(8, memberDto.getId());

			value = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	
	
}
