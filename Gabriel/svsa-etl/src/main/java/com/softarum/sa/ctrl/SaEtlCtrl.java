package com.softarum.sa.ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.maps.errors.ApiException;
import com.softarum.sa.dao.SaEtlDAO;
import com.softarum.sa.model.Atendimento;
import com.softarum.sa.model.Endereco;
import com.softarum.sa.model.Pessoa;
import com.softarum.sa.model.Prontuario;
import com.softarum.sa.model.Unidade;
import com.softarum.sa.util.GeocodingUtil;
import com.softarum.sa.util.HasUtil;
import com.softarum.sa.util.TemporalidadeUtil;

import svsa.dao.SvsaEtlDAO;
import svsa.to.SvsaDTO;

/**
 * 
 * @author Gabriel
 *
 */
public class SaEtlCtrl {

	private EntityManagerFactory emSa;
	private EntityManager managerSa;

	@Inject
	private SvsaEtlDAO svsaDAO = new SvsaEtlDAO();
	@Inject
	private SaEtlDAO saDAO = new SaEtlDAO();

	/*
	 * assim que o controlador for chamado, o método que cria o manager do banco SA
	 * é criado
	 */
	public SaEtlCtrl() {
		createManagers();
	}

	/*
	 * método extract, consulta o banco do SVSA e traz todos os atendimentos
	 */
	public List<SvsaDTO> extractDTO() throws SQLException {
		return svsaDAO.buscarTodosDTO();
	}

	/*
	 * método extract unidades
	 */
	public List<SvsaDTO> extractUnidades() throws SQLException {
		return svsaDAO.buscarUnidadesDTO();
	}

	/*
	 * método transform de unidades, recebe as unidadesDTO, trata os dados e joga no
	 * banco do SA
	 */
	public List<Unidade> transformUnidades(List<SvsaDTO> unidadesDTO)
			throws ApiException, InterruptedException, IOException {

		List<Unidade> unidades = new ArrayList<Unidade>();

		/*
		 * abre transação(transaction.begin());
		 */
		saDAO.abrirTransaction();

		for (SvsaDTO dto : unidadesDTO) {

			Unidade u = saDAO.buscarUnidadeCodigo(dto.getCodigoUnidade());
			if (u == null) {

				Endereco e = saDAO.buscarPeloCodigo(dto.getCodigoEndereco());
				if (e == null) {
					e = new Endereco(dto.getCodigoEndereco(), dto.getBairro(), dto.getCep(), dto.getEndereco(),
							dto.getNumero(), dto.getDataModificacao());

					/*
					 * conferindo se o objeto construido não possui colunas nulas ou vazias, pois a
					 * API só retorna as coordenadas certas se tiver todos os atributos verificados
					 * no IF abaixo
					 */
					if (HasUtil.content(e.getBairro()) && HasUtil.content(e.getCep())
							&& HasUtil.content(e.getEndereco()) && HasUtil.content(e.getNumero())) {
						GeocodingUtil.inserirCoordenadas(e);
					}

					saDAO.persistirEndereco(e);
				} else {

					/*
					 * verificação de nulos e vazios
					 */
					if (HasUtil.content(e.getBairro()) && HasUtil.content(e.getCep())
							&& HasUtil.content(e.getEndereco()) && HasUtil.content(e.getNumero())) {

						/*
						 * verifica se o objeto do SA está diferente do objeto do SVSA, verifica também
						 * se a data de modificação está dentro do período passado(neste caso 15 dias),
						 * se estiver, altera o objeto e deixa o do SA igual ao do SVSA
						 */
						if (e.getDataModificacao() != dto.getDataModificacao()
								&& TemporalidadeUtil.dentroPeriodo(e, 15)) {
							e.setBairro(dto.getBairro());
							e.setCep(dto.getCep());
							e.setEndereco(dto.getEndereco());
							e.setNumero(dto.getNumero());
							e.setDataModificacao(dto.getDataModificacao());
							GeocodingUtil.inserirCoordenadas(e);
						}
					}

				}

				u = new Unidade(dto.getCodigoUnidade(), dto.getNomeUnidade(), dto.getContato());
				u.setEndereco(e);

				saDAO.persistirUnidade(u);

				unidades.add(u);
			} else {

				/*
				 * verificação de nulos e vazios
				 */
				if (HasUtil.content(u.getEndereco().getBairro()) && HasUtil.content(u.getEndereco().getCep())
						&& HasUtil.content(u.getEndereco().getEndereco())
						&& HasUtil.content(u.getEndereco().getNumero())) {

					/*
					 * verificação de objetos diferentes, if (objeto(SVSA) != objeto(SA) && alterado
					 * dentro de 15 dias)
					 */
					if (u.getEndereco().getDataModificacao() != dto.getDataModificacao()
							&& TemporalidadeUtil.dentroPeriodo(u.getEndereco(), 15) == true) {
						u.getEndereco().setBairro(dto.getBairro());
						u.getEndereco().setCep(dto.getCep());
						u.getEndereco().setEndereco(dto.getEndereco());
						u.getEndereco().setDataModificacao(dto.getDataModificacao());
						GeocodingUtil.inserirCoordenadas(u.getEndereco());
						saDAO.persistirUnidade(u);
					}
				}

				unidades.add(u);
			}

		}

		saDAO.fecharTransaction();

		return unidades;
	}

	/*
	 * método transform que recebe a lista de atendimentos que foi trazida do SVSA e
	 * salva em uma lista que será persistida no banco do SA
	 */
	public List<Atendimento> transformAtendimento(List<SvsaDTO> listaDTO)
			throws ApiException, InterruptedException, IOException {

		List<Atendimento> atendimentos = new ArrayList<Atendimento>();

		/*
		 * abre transação(transaction.begin());
		 */
		saDAO.abrirTransaction();

		/*
		 * foreach que atribui para o banco do SA tudo que há no DTO, está comentado
		 * pois a cada 1000 requisições na API Geocoding custa 5 dólares, então
		 * cuidado!! a lógica dentro do for é simples, ao buscar um atendimento do dto
		 * no banco do SA, se o atendimento não existir no banco então deverá ser
		 * criado, e o mesmso funciona para outros objetos que o atendimento referência
		 * direta ou indiretamente
		 */
		/*
		 * for (SvsaDTO dto : listaDTO) {
		 * 
		 * Atendimento a = saDAO.buscarAtendimento(dto.getCodigoAtendimento()); if (a ==
		 * null) {
		 * 
		 * Endereco e = saDAO.buscarPeloCodigo(dto.getCodigoEndereco()); if (e == null)
		 * { e = new Endereco(dto.getCodigoEndereco(), dto.getBairro(), dto.getCep(),
		 * dto.getEndereco(), dto.getNumero(), dto.getDataModificacao());
		 * saDAO.persistirEndereco(e); }
		 * 
		 * Unidade u = saDAO.buscarUnidadeCodigo(dto.getCodigoUnidade()); if (u == null)
		 * { u = new Unidade(dto.getCodigoUnidade(), dto.getNomeUnidade(),
		 * dto.getContato()); u.setEndereco(e); saDAO.persistirUnidade(u); }
		 * 
		 * Prontuario pt = saDAO.buscarProntuario(dto.getCodigoProntuario()); if (pt ==
		 * null) { pt = new Prontuario(dto.getCodigoProntuario()); pt.setEndereco(e);
		 * pt.setUnidade(u); saDAO.persistirProntuario(pt); }
		 * 
		 * Pessoa pe = saDAO.buscarPessoa(dto.getCodigoPessoa()); if (pe == null) { pe =
		 * new Pessoa(dto.getCodigoPessoa(), dto.getNomePessoa()); pe.setProntuario(pt);
		 * saDAO.persistirPessoa(pe); }
		 * 
		 * a = new Atendimento(dto.getCodigoAtendimento(), dto.getCodigoAuxiliar(),
		 * dto.getDataAtendimento()); a.setPessoa(pe);
		 * 
		 * saDAO.persistirAtendimento(a);
		 * 
		 * atendimentos.add(a); } else {
		 * 
		 * saDAO.persistirAtendimento(a);
		 * 
		 * atendimentos.add(a); }
		 * 
		 * }
		 */

		/*
		 * for que retorna 100 atendimentos(criado para testes de API e geração de mapas
		 * em python) precisamos testar endereços de diferentes unidades, por isso a
		 * repetição é feita de 100 em 100 registros
		 */
		for (int i = 100; i < 10000; i = i + 100) {

			Atendimento a = saDAO.buscarAtendimento(listaDTO.get(i).getCodigoAtendimento());
			if (a == null) {

				Endereco e = saDAO.buscarPeloCodigo(listaDTO.get(i).getCodigoEndereco());
				if (e == null) {
					e = new Endereco(listaDTO.get(i).getCodigoEndereco(), listaDTO.get(i).getBairro(),
							listaDTO.get(i).getCep(), listaDTO.get(i).getEndereco(), listaDTO.get(i).getNumero(),
							listaDTO.get(i).getDataModificacao());

					/*
					 * conferindo se o objeto construido não possui colunas nulas ou vazias, pois a
					 * API só retorna as coordenadas certas se tiver todos os atributos verificados
					 * no IF abaixo
					 */
					if (HasUtil.content(e.getBairro()) && HasUtil.content(e.getCep())
							&& HasUtil.content(e.getEndereco()) && HasUtil.content(e.getNumero())) {
						GeocodingUtil.inserirCoordenadas(e);
					}

					saDAO.persistirEndereco(e);
				} else {

					/*
					 * verificação de nulos e vazios
					 */
					if (HasUtil.content(e.getBairro()) && HasUtil.content(e.getCep())
							&& HasUtil.content(e.getEndereco()) && HasUtil.content(e.getNumero())) {

						/*
						 * verifica se o objeto do SA está diferente do objeto do SVSA, verifica também
						 * se a data de modificação está dentro do período passado(neste caso 15 dias),
						 * se estiver, altera o objeto e deixa o do SA igual ao do SVSA
						 */
						if (e.getDataModificacao() != listaDTO.get(i).getDataModificacao()
								&& TemporalidadeUtil.dentroPeriodo(e, 15)) {
							e.setBairro(listaDTO.get(i).getBairro());
							e.setCep(listaDTO.get(i).getCep());
							e.setEndereco(listaDTO.get(i).getEndereco());
							e.setNumero(listaDTO.get(i).getNumero());
							e.setDataModificacao(listaDTO.get(i).getDataModificacao());
							GeocodingUtil.inserirCoordenadas(e);
						}
					}
				}

				Unidade u = saDAO.buscarUnidadeCodigo(listaDTO.get(i).getCodigoUnidade());
				if (u == null) {
					u = new Unidade(listaDTO.get(i).getCodigoUnidade(), listaDTO.get(i).getNomeUnidade(),
							listaDTO.get(i).getContato());
					u.setEndereco(e);
					saDAO.persistirUnidade(u);
				}

				Prontuario pt = saDAO.buscarProntuario(listaDTO.get(i).getCodigoProntuario());
				if (pt == null) {
					pt = new Prontuario(listaDTO.get(i).getCodigoProntuario());
					pt.setEndereco(e);
					pt.setUnidade(u);
					saDAO.persistirProntuario(pt);
				}

				Pessoa pe = saDAO.buscarPessoa(listaDTO.get(i).getCodigoPessoa());
				if (pe == null) {
					pe = new Pessoa(listaDTO.get(i).getCodigoPessoa(), listaDTO.get(i).getNomePessoa());
					pe.setProntuario(pt);
					saDAO.persistirPessoa(pe);
				}

				a = new Atendimento(listaDTO.get(i).getCodigoAtendimento(), listaDTO.get(i).getCodigoAuxiliar(),
						listaDTO.get(i).getDataAtendimento());
				a.setPessoa(pe);

				saDAO.persistirAtendimento(a);

				atendimentos.add(a);

			} else {

				/*
				 * verificação de nulos e vazios
				 */
				if (HasUtil.content(a.getPessoa().getProntuario().getEndereco().getBairro())
						&& HasUtil.content(a.getPessoa().getProntuario().getEndereco().getCep())
						&& HasUtil.content(a.getPessoa().getProntuario().getEndereco().getEndereco())
						&& HasUtil.content(a.getPessoa().getProntuario().getEndereco().getNumero())) {

					/*
					 * verificação de objetos diferentes, if (objeto(SVSA) != objeto(SA) && alterado
					 * dentro de 15 dias)
					 */
					if (a.getPessoa().getProntuario().getEndereco().getDataModificacao() != listaDTO.get(i)
							.getDataModificacao()
							&& TemporalidadeUtil.dentroPeriodo(a.getPessoa().getProntuario().getEndereco(),
									15) == true) {
						a.getPessoa().getProntuario().getEndereco().setBairro(listaDTO.get(i).getBairro());
						a.getPessoa().getProntuario().getEndereco().setCep(listaDTO.get(i).getCep());
						a.getPessoa().getProntuario().getEndereco().setEndereco(listaDTO.get(i).getEndereco());
						a.getPessoa().getProntuario().getEndereco()
								.setDataModificacao(listaDTO.get(i).getDataModificacao());
						GeocodingUtil.inserirCoordenadas(a.getPessoa().getProntuario().getEndereco());
						saDAO.persistirAtendimento(a);
					}
				}

				atendimentos.add(a);
			}

		}

		/*
		 * fecha transação(transaction.commit());
		 */
		saDAO.fecharTransaction();

		return atendimentos;
	}

	/*
	 * método que cria o manager de acesso ao banco do SA
	 */
	private void createManagers() {
		emSa = Persistence.createEntityManagerFactory("saPU");

		managerSa = emSa.createEntityManager();

		saDAO.setManager(managerSa);
	}

	/*
	 * método que fecha o manager criado
	 */
	public void close() {
		managerSa.close();
	}
}