package svsa.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Gabriel - classe que fabrica uma conexão com o banco de dados do SVSA
 */
public class SvsaConnectionFactory {
	public Connection getConnection() {
		try {
			/*
			 * credenciais do banco: url, login, senha do banco respectivamente
			 */
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/svsa_salto?useSSL=false&serverTimezone=UTC", "root",
					"ifsp");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}