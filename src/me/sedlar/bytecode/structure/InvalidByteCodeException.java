/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class InvalidByteCodeException extends Exception {

	/**
	 * Constructor.
	 */
	public InvalidByteCodeException() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param message
	 *            the message
	 */
	public InvalidByteCodeException(String message) {
		super(message);
	}
}
