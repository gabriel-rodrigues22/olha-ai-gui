package svsa.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import svsa.to.SvsaDTO;

/**
 * @author murakamiadmin refactored by gabriel
 *
 */
public class SvsaEtlDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 * retorna uma lista de unidades do banco do SVSA
	 */
	public List<SvsaDTO> buscarUnidadesDTO() throws SQLException{
		
		/*
		 * abre uma conexão com o banco do SVSA
		 */
		Connection con = new SvsaConnectionFactory().getConnection();
		
		/*
		 * consulta que será executada no banco do SVSA
		 */
		String sql = "SELECT " + "unidade.codigo AS 'codigo_unidade', " + "unidade.nome AS 'nome_unidade', "
				+ "unidade.contato AS 'contato_unidade', " + "endereco.codigo AS 'codigo_endereco', "
				+ "endereco.bairro AS 'bairro', " + "endereco.cep AS 'cep', " + "endereco.endereco AS 'endereco', "
				+ "endereco.numero AS 'numero', " + "endereco.dataModificacao AS 'data_modificacao' "
				+ "FROM unidade, endereco WHERE unidade.codigo_endereco = endereco.codigo";
		
		/*
		 * recebe um comando SQL pré-compilado, assim pode ser executado diversas vezes
		 */
		PreparedStatement ps = con.prepareStatement(sql);

		/*
		 * recebe uma tabela de dados
		 */
		ResultSet rs = ps.executeQuery();

		/*
		 * inicia lista que receberá o resulta da query executada
		 */
		List<SvsaDTO> unidades = new ArrayList<SvsaDTO>();

		/*
		 * percorre toda a tabela ResultSet
		 */
		while (rs.next()) {

			SvsaDTO obj = new SvsaDTO(rs.getLong("codigo_unidade"), rs.getString("nome_unidade"),
					rs.getString("contato_unidade"), rs.getLong("codigo_endereco"), rs.getString("bairro"),
					rs.getString("cep"), rs.getString("endereco"), rs.getString("numero"),
					rs.getDate("data_modificacao"));

			unidades.add(obj);
		}

		/*
		 * fecha as conexões abertas
		 */
		con.close();
		ps.close();
		rs.close();

		return unidades;
		
	}

	/*
	 * retorna uma lista de atendimentos do banco do SVSA
	 */
	public List<SvsaDTO> buscarTodosDTO() throws SQLException {

		/*
		 * abre uma conexão com o banco do SVSA
		 */
		Connection con = new SvsaConnectionFactory().getConnection();

		/*
		 * consulta que será executada no banco do SVSA
		 */
		String sql = "SELECT " + "lista.codigo AS 'codigo_atendimento', "
				+ "lista.codigoAuxiliar AS 'codigo_auxiliar', " + "lista.dataAtendimento AS 'data_atendimento', "
				+ "pessoa.codigo AS 'codigo_pessoa', " + "pessoa.nome AS 'nome_pessoa', "
				+ "prontuario.codigo AS 'codigo_prontuario', " + "unidade.codigo AS 'codigo_unidade', "
				+ "unidade.nome AS 'nome_unidade', " + "unidade.contato AS 'contato_unidade', "
				+ "endereco.codigo AS 'codigo_endereco', " + "endereco.bairro AS 'bairro', " + "endereco.cep AS 'cep', "
				+ "endereco.endereco AS 'endereco', " + "endereco.numero AS 'numero', "
				+ "endereco.dataModificacao AS 'data_modificacao' " + "FROM " + "prontuario, " + "unidade, "
				+ "endereco, " + "familia, " + "pessoa, " + "listaatendimento as lista " + "WHERE "
				+ "lista.codigo_pessoa = pessoa.codigo " + "AND pessoa.codigo_familia = familia.codigo "
				+ "AND familia.codigo_endereco = endereco.codigo "
				+ "AND familia.codigo_prontuario = prontuario.codigo "
				+ "AND prontuario.codigo_unidade = unidade.codigo " + "AND lista.statusAtendimento = 'ATENDIDO'";

		/*
		 * recebe um comando SQL pré-compilado, assim pode ser executado diversas vezes
		 */
		PreparedStatement ps = con.prepareStatement(sql);

		/*
		 * recebe uma tabela de dados
		 */
		ResultSet rs = ps.executeQuery();

		/*
		 * inicia lista que receberá o resulta da query executada
		 */
		List<SvsaDTO> lista = new ArrayList<SvsaDTO>();

		/*
		 * percorre toda a tabela ResultSet
		 */
		while (rs.next()) {
			SvsaDTO obj = new SvsaDTO(rs.getLong("codigo_atendimento"), rs.getString("codigo_auxiliar"),
					rs.getDate("data_atendimento"), rs.getLong("codigo_pessoa"), rs.getString("nome_pessoa"),
					rs.getLong("codigo_prontuario"), rs.getLong("codigo_unidade"), rs.getString("nome_unidade"),
					rs.getString("contato_unidade"), rs.getLong("codigo_endereco"), rs.getString("bairro"),
					rs.getString("cep"), rs.getString("endereco"), rs.getString("numero"),
					rs.getDate("data_modificacao"));

			lista.add(obj);
		}

		/*
		 * fecha as conexões abertas
		 */
		con.close();
		ps.close();
		rs.close();

		return lista;
	}

}