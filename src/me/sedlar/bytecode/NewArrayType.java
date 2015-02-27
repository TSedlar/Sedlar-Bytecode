/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public enum NewArrayType {

	BOOLEAN(4, "boolean"),
	CHAR(5, "char"),
	FLOAT(6, "float"),
	DOUBLE(7, "double"),
	BYTE(8, "byte"),
	SHORT(9, "short"),
	INT(10, "int"),
	LONG(11, "long");

	public static NewArrayType fromCode(int code) {
		for (NewArrayType newArrayType : values()) {
			if (newArrayType.code == code) {
				return newArrayType;
			}
		}
		return null;
	}

	private int code;
	private String verbose;

	NewArrayType(int code, String verbose) {
		this.code = code;
		this.verbose = verbose;
	}

	/**
	 * Returns the immediate byte variable.
	 *
	 * @return the variable
	 */
	public int code() {
		return code;
	}

	/**
	 * Returns the verbose representation.
	 *
	 * @return the text
	 */
	public String verbose() {
		return verbose;
	}
}
