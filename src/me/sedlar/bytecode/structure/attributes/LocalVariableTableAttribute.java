/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.IOException;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class LocalVariableTableAttribute extends LocalVariableCommonAttribute {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "LocalVariableTable";

	/**
	 * Get the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableTableEntry</tt> structure.
	 *
	 * @return the array
	 */
	public LocalVariableTableEntry[] localVariableTable() {
		return (LocalVariableTableEntry[]) localVariableTable;
	}

	/**
	 * Set the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableTableEntry</tt> structure.
	 *
	 * @param localVariableTable
	 *            the index
	 */
	public void setLocalVariableTable(LocalVariableTableEntry[] localVariableTable) {
		this.localVariableTable = localVariableTable;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int localVariableTableLength = in.readUnsignedShort();
		localVariableTable = new LocalVariableTableEntry[localVariableTableLength];
		for (int i = 0; i < localVariableTableLength; i++)
			localVariableTable[i] = LocalVariableTableEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public int length() {
		return INITIAL_LENGTH + length(localVariableTable) * LocalVariableTableEntry.LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "LocalVariableTable attribute with " + length(localVariableTable) + " entries");
	}
}
