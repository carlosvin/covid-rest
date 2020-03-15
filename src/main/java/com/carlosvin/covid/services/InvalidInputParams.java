package com.carlosvin.covid.services;


public class InvalidInputParams extends Exception {

	public InvalidInputParams(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}