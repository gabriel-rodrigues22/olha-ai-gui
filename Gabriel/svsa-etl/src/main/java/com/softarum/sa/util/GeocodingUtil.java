package com.softarum.sa.util;

import java.io.IOException;
import java.io.Serializable;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.softarum.sa.model.Endereco;

/**
 * @author gabriel
 *
 */
public class GeocodingUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * método que recebe um único objeto endereco, chama a API do Google e setta as
	 * coordenadas do objeto recebido, cuidado ao fazer chamadas nesse método, pois
	 * a cada 1000 requisições na API, é cobrado pelo Google 5 dólares
	 */
	public static Endereco inserirCoordenadas(Endereco e) throws ApiException, InterruptedException, IOException {

		/*
		 * API Google Maps - apiKey = chave da API gerada pela plataforma do Google
		 */
		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyBAIeepM8_5OhSkKAfAhDM_CtSnNy91djg").build();

		/*
		 * concatenando string para passar como parâmetro
		 */
		String s = e.getNumero() + " " + e.getEndereco() + " " + e.getBairro() + " " + e.getCep();

		GeocodingResult[] results = GeocodingApi.geocode(context, s).await();

		if (results != null && results.length > 0) {
			if (results[0].geometry.location.lat != 0 && results[0].geometry.location.lng != 0) {
				/*
				 * results[0].geometry.location.lat/lng retorna um double, usando valueOf é
				 * possível converter para String
				 */
				String latitude = String.valueOf(results[0].geometry.location.lat);
				String longitude = String.valueOf(results[0].geometry.location.lng);
				e.setLatitude(latitude);
				e.setLongitude(longitude);
				return e;
			}
		}

		return e;
	}
}