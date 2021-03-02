package com.softarum.sa.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author murakamiadmin
 *
 */
public class LogUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger logger;

	public LogUtil(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	private void add(String message, int i) {

		if (i == 0)
			logger.info(message);
		else
			logger.error(message);
	}

	public void info(String message) {
		add(message, 0);
	}

	public void error(String message) {
		add(message, 1);
	}
}