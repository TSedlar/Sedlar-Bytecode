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
public class LocalVariableTypeTableAttribute extends LocalVariableCommonAttribute {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "LocalVariableTypeTable";

	/**
	 * Get the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableTypeTableEntry</tt> structure.
	 *
	 * @return the array
	 */
	public LocalVariableTypeTableEntry[] localVariableTypeTable() {
		return (LocalVariableTypeTableEntry[]) localVariableTable;
	}

	/**
	 * Set the list of local variable associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LocalVariableTypeTableEntry</tt> structure.
	 *
	 * @param localVariableTypeTable
	 *            the array
	 */
	public void setLocalVariableTypeTable(LocalVariableTypeTableEntry[] localVariableTypeTable) {
		this.localVariableTable = localVariableTypeTable;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int localVariableTypeTableLength = in.readUnsignedShort();
		localVariableTable = new LocalVariableTypeTableEntry[localVariableTypeTableLength];
		for (int i = 0; i < localVariableTypeTableLength; i++)
			localVariableTable[i] = LocalVariableTypeTableEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public int length() {
		return INITIAL_LENGTH + length(localVariableTable) * LocalVariableTypeTableEntry.LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "LocalVariableTypeTable attribute with " + length(localVariableTable) + " entries");
	}
}
