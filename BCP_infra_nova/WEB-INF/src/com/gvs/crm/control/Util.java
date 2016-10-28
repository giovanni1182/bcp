package com.gvs.crm.control;

public class Util {
	public static String translateException(Exception exception) {
		String message = exception.getMessage();
		if (message == null)
			message = "Erro Interno: " + exception.getClass().getName();
		return message;
	}
}