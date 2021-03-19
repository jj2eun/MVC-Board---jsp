package com.java.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {
	public static Connection getConnection() {
		
		Connection conn = null;
		
		try {
			// 오라클 버전에 따라 sid가 다르다 -> oracle 11g : orcd , oracle 11xe : xe
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "mvc";
			String pass = "mvc";

			conn = DriverManager.getConnection(url,id,pass);
		} catch (Exception e) {
			System.out.println("로딩 실패");
		}
		return conn;
	}
}
