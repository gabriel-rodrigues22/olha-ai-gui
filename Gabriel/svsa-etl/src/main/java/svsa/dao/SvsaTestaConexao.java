package svsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author Gabriel - classe que testa a conexão ao banco de dados do SVSA
 *
 */
public class SvsaTestaConexao {

	public static void main(String[] args) throws SQLException {

		Connection con = new SvsaConnectionFactory().getConnection();
		System.out.println("Conexão aberta!");
		con.close();

	}

}