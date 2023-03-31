package com.HappySchool.Project.servicesException;

public class MethodArgumentNotValidExceptions extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MethodArgumentNotValidExceptions(String msg) {
		super(msg);
	}
}