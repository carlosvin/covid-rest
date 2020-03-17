package com.carlosvin.covid.services.exceptions;


public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String msg) {
		super(msg);
	}
}