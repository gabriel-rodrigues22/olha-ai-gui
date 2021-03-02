package com.softarum.sa.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.softarum.sa.model.Atendimento;
import com.softarum.sa.model.Endereco;
import com.softarum.sa.model.Pessoa;
import com.softarum.sa.model.Prontuario;
import com.softarum.sa.model.Unidade;
import com.softarum.sa.util.LogUtil;

/**
 * @author murakamiadmin refactored by gabriel
 *
 */
public class SaEtlDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private EntityTransaction transaction;

	@Inject
	private EntityManager manager;

	private LogUtil log = new LogUtil(SaEtlDAO.class);

	public Endereco buscarPeloCodigo(Long codigo) {
		return manager.find(Endereco.class, codigo);
	}

	public Unidade buscarUnidadeCodigo(Long codigo) {
		return manager.find(Unidade.class, codigo);
	}

	public Atendimento buscarAtendimento(Long codigo) {
		return manager.find(Atendimento.class, codigo);
	}

	public Pessoa buscarPessoa(Long codigo) {
		return manager.find(Pessoa.class, codigo);
	}

	public Prontuario buscarProntuario(Long codigo) {
		return manager.find(Prontuario.class, codigo);
	}

	public void abrirTransaction() {
		transaction.begin();
	}

	public void persistirEndereco(Endereco e) {
		manager.persist(e);
	}

	public void persistirUnidade(Unidade u) {
		manager.persist(u);
	}

	public void persistirProntuario(Prontuario pt) {
		manager.persist(pt);
	}

	public void persistirPessoa(Pessoa pe) {
		manager.persist(pe);
	}

	public void persistirAtendimento(Atendimento a) {
		manager.persist(a);
	}

	public void fecharTransaction() {
		log.info("transaction commit (fechando transação)");
		transaction.commit();
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
		transaction = manager.getTransaction();
	}
}