/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AttributeInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class LocalVariableCommonAttribute extends AttributeInfo {

	protected static final int INITIAL_LENGTH = 2;

	protected LocalVariableCommonEntry[] localVariableTable;

	/**
	 * Get the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableCommonEntry</tt> structure.
	 *
	 * @return the array
	 */
	public LocalVariableCommonEntry[] localVariables() {
		return localVariableTable;
	}

	/**
	 * Set the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableCommonEntry</tt> structure.
	 *
	 * @param localVariableEntries
	 *            the array
	 */
	public void setLocalVariables(LocalVariableCommonEntry[] localVariableEntries) {
		this.localVariableTable = localVariableEntries;
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int localVariableTableLength = length(localVariableTable);
		out.writeShort(localVariableTableLength);
		for (int i = 0; i < localVariableTableLength; i++)
			localVariableTable[i].write(out);
		if (debug)
			debug("wrote ");
	}
}
