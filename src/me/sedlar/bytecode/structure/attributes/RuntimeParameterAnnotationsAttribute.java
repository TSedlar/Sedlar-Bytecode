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

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class RuntimeParameterAnnotationsAttribute extends AttributeInfo {

	private static final int INITIAL_LENGTH = 1;

	private ParameterAnnotations[] parameterAnnotations;

	/**
	 * Get the list of parameter annotations of the parent structure as an array
	 * of <tt>ParameterAnnotations</tt> structure.
	 *
	 * @return the array
	 */
	public ParameterAnnotations[] annotations() {
		return parameterAnnotations;
	}

	/**
	 * Set the list of parameter annotations associations of the parent
	 * structure as an array of <tt>ParameterAnnotations</tt> structure.
	 *
	 * @param parameterAnnotations
	 *            the array
	 */
	public void setAnnotations(ParameterAnnotations[] parameterAnnotations) {
		this.parameterAnnotations = parameterAnnotations;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int numParameters = in.readUnsignedByte();
		parameterAnnotations = new ParameterAnnotations[numParameters];
		for (int i = 0; i < numParameters; i++) {
			parameterAnnotations[i] = new ParameterAnnotations();
			parameterAnnotations[i].read(in);
		}
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int parameterAnnotationsLength = length(parameterAnnotations);
		out.writeByte(parameterAnnotationsLength);
		for (int i = 0; i < parameterAnnotationsLength; i++)
			parameterAnnotations[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		int length = INITIAL_LENGTH;
		for (ParameterAnnotations annotation : parameterAnnotations)
			length += annotation.length();
		return length;
	}
}
