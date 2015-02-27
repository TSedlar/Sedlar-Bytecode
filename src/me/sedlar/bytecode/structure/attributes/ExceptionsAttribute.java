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
public class ExceptionsAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "Exceptions";

	private static final int INITIAL_LENGTH = 2;

	private int[] exceptionIndexTable;

	/**
	 * Get the list of exceptions thrown by the parent <tt>Code</tt> attribute
	 * as an array of indices into the constant pool.
	 *
	 * @return the array
	 */
	public int[] exceptionIndexTable() {
		return exceptionIndexTable;
	}

	/**
	 * Set the list of exceptions thrown by the parent <tt>Code</tt> attribute
	 * as an array of indices into the constant pool.
	 *
	 * @param exceptionIndexTable
	 *            the array
	 */
	public void setExceptionIndexTable(int[] exceptionIndexTable) {
		this.exceptionIndexTable = exceptionIndexTable;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int numberOfExceptions = in.readUnsignedShort();
		exceptionIndexTable = new int[numberOfExceptions];
		for (int i = 0; i < numberOfExceptions; i++)
			exceptionIndexTable[i] = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int numberOfExceptions = length(exceptionIndexTable);
		out.writeShort(numberOfExceptions);
		for (int i = 0; i < numberOfExceptions; i++)
			out.writeShort(exceptionIndexTable[i]);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return INITIAL_LENGTH + (length(exceptionIndexTable) * 2);
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "Exception attribute with " + length(exceptionIndexTable) + " exceptions");
	}
}
