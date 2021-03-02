package com.softarum.sa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author gabriel
 *
 */
@Entity
@Table(name = "Atendimento", schema = "sa_salto")
public class Atendimento implements Cloneable, Serializable {

	private static final long serialVersionUID = 2060033487029113003L;
	private Long codigo;
	private String codigoAuxiliar;
	private Date dataAtendimento;

	/*
	 * relacionamentos
	 */
	private Pessoa pessoa;

	/*
	 * construtores
	 */
	public Atendimento() {

	}

	public Atendimento(Long codigo, String codigoAuxiliar, Date dataAtendimento) {
		this.codigo = codigo;
		this.codigoAuxiliar = codigoAuxiliar;
		this.dataAtendimento = dataAtendimento;
	}

	@Id
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCodigoAuxiliar() {
		return codigoAuxiliar;
	}

	public void setCodigoAuxiliar(String codigoAuxiliar) {
		this.codigoAuxiliar = codigoAuxiliar;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	/*
	 * mapeamento
	 */
	@OneToOne(cascade = CascadeType.ALL)
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((codigoAuxiliar == null) ? 0 : codigoAuxiliar.hashCode());
		result = prime * result + ((dataAtendimento == null) ? 0 : dataAtendimento.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atendimento other = (Atendimento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (codigoAuxiliar == null) {
			if (other.codigoAuxiliar != null)
				return false;
		} else if (!codigoAuxiliar.equals(other.codigoAuxiliar))
			return false;
		if (dataAtendimento == null) {
			if (other.dataAtendimento != null)
				return false;
		} else if (!dataAtendimento.equals(other.dataAtendimento))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		return true;
	}

}