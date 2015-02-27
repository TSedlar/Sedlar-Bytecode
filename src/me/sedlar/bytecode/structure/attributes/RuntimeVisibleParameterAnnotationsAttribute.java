/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class RuntimeVisibleParameterAnnotationsAttribute extends RuntimeParameterAnnotationsAttribute {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations";

	@Override
	protected void debug(String message) {
		super.debug(message + "RuntimeVisibleParameterAnnotations attribute with " + length(annotations())
				+ " entries");
	}
}
