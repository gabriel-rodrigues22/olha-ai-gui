package svsa.to;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author gabriel
 *
 */
public class SvsaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;

	/*
	 * Atendimento
	 */
	private Long codigoAtendimento;
	private String codigoAuxiliar;
	private Date dataAtendimento;

	/*
	 * Pessoa
	 */
	private Long codigoPessoa;
	private String nomePessoa;

	/*
	 * Prontuario
	 */
	private Long codigoProntuario;

	/*
	 * Unidade
	 */
	private Long codigoUnidade;
	private String nomeUnidade;
	private String contato;

	/*
	 * Endereco
	 */
	private Long codigoEndereco;
	private String bairro;
	private String cep;
	private String endereco;
	private String numero;
	private Date dataModificacao;

	/*
	 * construtores
	 */

	public SvsaDTO() {
	}

	public SvsaDTO(Long codigoAtendimento, String codigoAuxiliar, Date dataAtendimento, Long codigoPessoa,
			String nomePessoa, Long codigoProntuario, Long codigoUnidade, String nomeUnidade, String contato,
			Long codigoEndereco, String bairro, String cep, String endereco, String numero, Date dataModificacao) {
		super();
		this.codigoAtendimento = codigoAtendimento;
		this.codigoAuxiliar = codigoAuxiliar;
		this.dataAtendimento = dataAtendimento;
		this.codigoPessoa = codigoPessoa;
		this.nomePessoa = nomePessoa;
		this.codigoProntuario = codigoProntuario;
		this.codigoUnidade = codigoUnidade;
		this.nomeUnidade = nomeUnidade;
		this.contato = contato;
		this.codigoEndereco = codigoEndereco;
		this.bairro = bairro;
		this.cep = cep;
		this.endereco = endereco;
		this.numero = numero;
		this.dataModificacao = dataModificacao;
	}

	public SvsaDTO(Long codigoUnidade, String nomeUnidade, String contato, Long codigoEndereco, String bairro,
			String cep, String endereco, String numero, Date dataModificacao) {
		super();
		this.codigoUnidade = codigoUnidade;
		this.nomeUnidade = nomeUnidade;
		this.contato = contato;
		this.codigoEndereco = codigoEndereco;
		this.bairro = bairro;
		this.cep = cep;
		this.endereco = endereco;
		this.numero = numero;
		this.dataModificacao = dataModificacao;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoAtendimento() {
		return codigoAtendimento;
	}

	public void setCodigoAtendimento(Long codigoAtendimento) {
		this.codigoAtendimento = codigoAtendimento;
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

	public Long getCodigoPessoa() {
		return codigoPessoa;
	}

	public void setCodigoPessoa(Long codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public Long getCodigoProntuario() {
		return codigoProntuario;
	}

	public void setCodigoProntuario(Long codigoProntuario) {
		this.codigoProntuario = codigoProntuario;
	}

	public Long getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(Long codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public Long getCodigoEndereco() {
		return codigoEndereco;
	}

	public void setCodigoEndereco(Long codigoEndereco) {
		this.codigoEndereco = codigoEndereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

}