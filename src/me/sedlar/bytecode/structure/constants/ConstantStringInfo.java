/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.ConstantPool;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ConstantStringInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 2;

	private int stringIndex;

	@Override
	public byte tag() {
		return CONSTANT_STRING;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_STRING_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return classInfo.constantPoolEntryName(stringIndex);
	}

	/**
	 * Get the index of the constant pool entry containing the string of this
	 * entry.
	 *
	 * @return the index
	 */
	public int stringIndex() {
		return stringIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the string of this
	 * entry.
	 *
	 * @param stringIndex
	 *            the index
	 */
	public void setStringIndex(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		stringIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_STRING);
		out.writeShort(stringIndex);
		if (debug)
			debug("wrote ");
	}

	protected void debug(String message) {
		super.debug(message + verboseTag() + " with string_index " + stringIndex);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantStringInfo))
			return false;
		ConstantStringInfo constantStringInfo = (ConstantStringInfo) object;
		return super.equals(object) && constantStringInfo.stringIndex == stringIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ stringIndex;
	}
}
