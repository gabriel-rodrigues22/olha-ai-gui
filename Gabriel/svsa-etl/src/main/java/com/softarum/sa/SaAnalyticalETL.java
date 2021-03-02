package com.softarum.sa;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.maps.errors.ApiException;
import com.softarum.sa.ctrl.SaEtlCtrl;
import com.softarum.sa.model.Atendimento;
import com.softarum.sa.model.Unidade;
import com.softarum.sa.util.LogUtil;

import svsa.to.SvsaDTO;

public class SaAnalyticalETL {

	@Inject
	private static SaEtlCtrl ctrl;
	private LogUtil log = new LogUtil(SaAnalyticalETL.class);

	public static void main(String[] args) throws SQLException, ApiException, InterruptedException, IOException {
		ctrl = new SaEtlCtrl();
		SaAnalyticalETL sa = new SaAnalyticalETL();

		/*
		 * ETL unidades - a parte do load está sendo feita na mesma repetição do
		 * transform, ao receber do DTO, já persiste no banco do SA se o objeto não
		 * existir no mesmo
		 */

		/*
		 * chamando E - extract, populando o DTO
		 */
		List<SvsaDTO> unidadesDTO = sa.extractUnidadesDTO();

		/*
		 * chamando T, L - transform e load, passa a lista de objetos DTO e o método
		 * transform consulta se o objeto DTO já existe no banco do SA, se não existir,
		 * o mesmo é criado e persistido(load)
		 */
		List<Unidade> unidades = new ArrayList<Unidade>();

		try {
			unidades = sa.transformUnidades(unidadesDTO);
		} catch (ApiException ae) {
			// TODO: handle exception
		} catch (InterruptedException ie) {
			// TODO: handle exception
		} catch (IOException ioe) {
			// TODO: handle exception
		}

		/*
		 * o método transform pode ser alterado para tipo void, pois não é necessário
		 * retornar a lista de atendimentos para o main, já que o objeto é persistido no
		 * método transform, no entanto, futuros tratamentos de excessões ou novas
		 * funcionalidades talvez dependam ser tratadas aqui no método main
		 */
		sa.log.info("(class: SaAnalyticalETL.java) Tamanho da Unidades DTO = " + unidadesDTO.size());
		sa.log.info("(class: SaAnalyticalETL.java) Tamanho da Lista de Unidades = " + unidades.size());

		/*
		 * ETL atendimentos - a parte do load está sendo feita na mesma repetição do
		 * transform, ao receber do DTO, já persiste no banco do SA se o objeto não
		 * existir no mesmo
		 */

		/*
		 * chamando E - extract, populando o DTO
		 */
		List<SvsaDTO> listaDTO = sa.extractDTO();

		/*
		 * chamando T, L - transform e load, passa a lista de objetos DTO e o método
		 * transform consulta se o objeto DTO já existe no banco do SA, se não existir,
		 * o mesmo é criado e persistido(load)
		 */
		List<Atendimento> atendimentos = new ArrayList<Atendimento>();

		try {
			atendimentos = sa.transformAtendimentos(listaDTO);
		} catch (ApiException ae) {
			// TODO: handle exception
		} catch (InterruptedException ie) {
			// TODO: handle exception
		} catch (IOException ioe) {
			// TODO: handle exception
		}

		/*
		 * o método transform pode ser alterado para tipo void, pois não é necessário
		 * retornar a lista de atendimentos para o main, já que o objeto é persistido no
		 * método transform, no entanto, futuros tratamentos de excessões ou novas
		 * funcionalidades talvez dependam ser tratadas aqui no método main
		 */
		sa.log.info("(class: SaAnalyticalETL.java) Tamanho da Lista DTO = " + listaDTO.size());
		sa.log.info("(class: SaAnalyticalETL.java) Tamanho da Lista de Atendimentos = " + atendimentos.size());

		ctrl.close();

		System.exit(0);
	}

	public List<SvsaDTO> extractUnidadesDTO() throws SQLException {
		log.info("Extract Unidades DTO (SaAnalytical.java)");
		return ctrl.extractUnidades();
	}

	public List<Unidade> transformUnidades(List<SvsaDTO> unidadesDTO)
			throws ApiException, InterruptedException, IOException {
		log.info("Transform Unidades (SaAnalyticalETL.java)");
		return ctrl.transformUnidades(unidadesDTO);
	}

	public List<SvsaDTO> extractDTO() throws SQLException {
		log.info("Extract DTO (SaAnalyticalETL.java)");
		return ctrl.extractDTO();
	}

	public List<Atendimento> transformAtendimentos(List<SvsaDTO> listaDTO)
			throws ApiException, InterruptedException, IOException {
		log.info("Transform Atendimentos (SaAnalyticalETL.java)");
		return ctrl.transformAtendimento(listaDTO);
	}

}