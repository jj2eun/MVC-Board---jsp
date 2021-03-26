package com.java.board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.java.database.ConnectionProvider;
import com.java.database.JDBCUtil;

public class BoardDao {

	private static BoardDao instance = new BoardDao();
	
	public static BoardDao getInstance() {
		
		return instance;
	}

	public int insert(BoardDto boardDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;
			
		WriteNumber(boardDto, conn);	// 첫 번째 답글일경우, sequence_number = 1, sequence_level = 1 
	
		try {
			String sql = "insert into board(board_number, writer, subject, email, content, password,"
					+ "write_date, read_count, group_number, sequence_number, sequence_level, file_name, path, file_size)"
					+ " values(board_number_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardDto.getWriter());
			pstmt.setString(2, boardDto.getSubject());
			pstmt.setString(3, boardDto.getEmail());
			pstmt.setString(4, boardDto.getContent().replace("\r\n", "<br>"));
			pstmt.setString(5, boardDto.getPassword());
			pstmt.setTimestamp(6, new Timestamp(boardDto.getWriteDate().getTime()));
			pstmt.setInt(7, boardDto.getReadCount());
			pstmt.setInt(8, boardDto.getGroupNumber());
			pstmt.setInt(9, boardDto.getSequenceNumber());
			pstmt.setInt(10, boardDto.getSequenceLevel());
			
			pstmt.setString(11, boardDto.getFileName());
			pstmt.setString(12, boardDto.getPath());
			pstmt.setLong(13, boardDto.getFileSize());
			
		
			value = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		return value;
	}

	private void WriteNumber(BoardDto boardDto, Connection conn) {
		// 그룹번호, 글순서(자식), 글레벨(자식) - 답변의 경우
													// 새 글			// 답글
		int boardNumber = boardDto.getBoardNumber(); // 0	133 		 144  
		int groupNumber = boardDto.getGroupNumber(); // 1	99			99	
		int sequenceNumber = boardDto.getSequenceNumber(); // 0 0		1	
		int sequenceLevel = boardDto.getSequenceLevel(); // 0  0		1
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			if(boardNumber == 0) { 	// 부모글의 그룹번호
				// 부모글을 작성할때
				sql = "select max(group_number) from board";
				conn = ConnectionProvider.getConnection();
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					// 그룹번호 ++1
					int max = rs.getInt(1);
					boardDto.setGroupNumber(max + 1);
				}
			} else {
				// 답글 : 글 순서, 글레벨 세팅
				sql = "update board set sequence_number = sequence_number + 1"
						+ " where group_number=? and sequence_number > ?";
				conn = ConnectionProvider.getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, groupNumber);
				pstmt.setInt(2, sequenceNumber);
				
				pstmt.executeUpdate();
				
				sequenceNumber += 1;
				sequenceLevel += 1;
				
				boardDto.setSequenceNumber(sequenceNumber);
				boardDto.setSequenceLevel(sequenceLevel);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		
	}

	public int getCount() {
		int value = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select count(*) from board";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				value = rs.getInt(1);
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

	public ArrayList<BoardDto> getBoardList(int startRow, int endRow) {
		
		ArrayList<BoardDto> boardList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from("
					+ "select rownum as rnum, a.*"
					+ "from ( select * from board order by group_number desc, sequence_number asc) a "
					+ ") b "
					+ "where b.rnum >= ? and b.rnum <= ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			
			boardList = new ArrayList<BoardDto>();
			
			while(rs.next()) {
				BoardDto boardDto = new BoardDto();
				boardDto.setBoardNumber(rs.getInt("board_number"));
				boardDto.setWriter(rs.getString("writer"));
				boardDto.setSubject(rs.getString("subject"));
				boardDto.setEmail(rs.getString("email"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setPassword(rs.getString("password"));
				boardDto.setWriteDate(new Date(rs.getTimestamp("write_date").getTime()));
				boardDto.setReadCount(rs.getInt("read_count"));
				boardDto.setGroupNumber(rs.getInt("group_number"));
				boardDto.setSequenceNumber(rs.getInt("sequence_number"));
				boardDto.setSequenceLevel(rs.getInt("sequence_level"));
				
				boardList.add(boardDto);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return boardList;
	}

	public BoardDto read(int boardNumber) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDto boardDto = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // autoCommit 꺼주기
			// 조회수 + 1 증가 쿼리문
			String sqlUpdate = "update board set read_count = read_count + 1 where board_number = ?";
			pstmt = conn.prepareStatement(sqlUpdate);
			pstmt.setInt(1, boardNumber);
			
			int value = pstmt.executeUpdate();
			
			if(value > 0) {
				// 조회수 증가 업데이트 쿼리 정상작동 완료
				String sqlSelect = "select * from board where board_number = ?";
				pstmt = conn.prepareStatement(sqlSelect);
				pstmt.setInt(1, boardNumber);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					boardDto = new BoardDto();
					boardDto.setBoardNumber(rs.getInt("board_number"));
					boardDto.setWriter(rs.getString("writer"));
					boardDto.setSubject(rs.getString("subject"));
					boardDto.setEmail(rs.getString("email"));
					boardDto.setContent(rs.getString("content"));
					boardDto.setPassword(rs.getString("password"));
					boardDto.setWriteDate(new Date(rs.getTimestamp("write_date").getTime()));
					
					boardDto.setReadCount(rs.getInt("read_count"));
					boardDto.setGroupNumber(rs.getInt("group_number"));
					boardDto.setSequenceNumber(rs.getInt("sequence_number"));
					boardDto.setSequenceLevel(rs.getInt("sequence_level"));
					
					boardDto.setFileName(rs.getString("file_name"));
					boardDto.setFileSize(rs.getLong("file_size"));
					boardDto.setPath(rs.getString("path"));
				}
				conn.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// 쿼리문이 정상작동되지 않을 경우 -> 롤백
			JDBCUtil.rollback(conn);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
			
		}
		
		return boardDto;
	}
	// 글수정 버튼 클릭시 수정할 게시글 가져오기
	public BoardDto updFunc(int boardNumber) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDto boardDto = null;
		
		try {
			String sql = "select * from board where board_number = ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNumber);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				boardDto = new BoardDto();
				boardDto.setBoardNumber(rs.getInt("board_number"));
				boardDto.setWriter(rs.getString("writer"));
				boardDto.setSubject(rs.getString("subject"));
				boardDto.setEmail(rs.getString("email"));
				boardDto.setContent(rs.getString("content").replace("<br>", "\r\n"));
				boardDto.setPassword(rs.getString("password"));
				boardDto.setWriteDate(new Date(rs.getTimestamp("write_date").getTime()));
				
				boardDto.setReadCount(rs.getInt("read_count"));
				boardDto.setGroupNumber(rs.getInt("group_number"));
				boardDto.setSequenceNumber(rs.getInt("sequence_number"));
				boardDto.setSequenceLevel(rs.getInt("sequence_level"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		return boardDto;
	}
	// 수정완료버튼 클릭시 게시글 업데이트
	public int update(String boardNumber, String subject, String email, String content, String password) {
		
		Connection conn= null;
		PreparedStatement pstmt = null;
		int value = 0;
		
		try {
			String sql = "update board set subject = ?, email =?, content = ?, password = ? where board_number = ?";
			System.out.println("content : " + content);
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, email);
			pstmt.setString(3, content.replace("\r\n", "<br>"));
			pstmt.setString(4, password);
			pstmt.setString(5, boardNumber);
			
			value = pstmt.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		System.out.println("value : "+ value);
		return value;
	}

	public int delete(String boardNumber, String password) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;
		
		try {
			String sql = "delete from board where board_number = ? and password = ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardNumber);
			pstmt.setString(2, password);
			
			value = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt);
			JDBCUtil.close(conn);
		}
		
		return value;
	}
	
}
