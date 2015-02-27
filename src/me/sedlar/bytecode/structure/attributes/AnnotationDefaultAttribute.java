/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AttributeInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.values.Value;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class AnnotationDefaultAttribute extends AttributeInfo {
	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "AnnotationDefault";

	private Value defaultValue;

	/**
	 * Get the <tt>default_value</tt> of this attribute.
	 *
	 * @return the <tt>default_value</tt>
	 */
	public Value value() {
		return defaultValue;
	}

	/**
	 * Set the <tt>default_value</tt> of this attribute.
	 *
	 * @param defaultValue
	 *            the <tt>default_value</tt>
	 */
	public void setValue(Value defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		defaultValue = Value.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		defaultValue.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return defaultValue.length();
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "AnnotationDefaultAttribute");
	}
}
