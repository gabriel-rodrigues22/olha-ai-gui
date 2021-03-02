package com.softarum.sa.util;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;

import com.softarum.sa.model.Endereco;

/**
 * @author gabriel
 *
 */
public class TemporalidadeUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * método que valida se a data de modificação de um endereço está dentro do
	 * período em dias passado
	 */
	public static boolean dentroPeriodo(Endereco e, int qtdDiasAnterior) {
		DateTime dataAtual = new DateTime();

		Date intervalo = new Date();

		/*
		 * o método minusDays do framework JodaTime recebe um inteiro como parâmetro que
		 * é a quantidade de dias subtraído ao objeto que chama o método ex:
		 * 30/01/2000.minusDays(15) == 15/01/2000 30/01/2000 = objeto que chama o método
		 * 15 = quantidade de dias subtraído 15/01/2000 = resultado da operação
		 */
		intervalo = dataAtual.minusDays(qtdDiasAnterior).toDate();

		/*
		 * compareTo retorna 0 se as datas forem iguais, retorna 1 se a data do
		 * argumento for menor e retorna -1 se a data de argumento for maior
		 */
		if (e.getDataModificacao().compareTo(intervalo) == 1 || e.getDataModificacao().compareTo(intervalo) == 0) {
			return true;
		} else {
			return false;
		}
	}

}