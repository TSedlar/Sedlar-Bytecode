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
public class LineNumberTableAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "LineNumberTable";

	private static final int INITIAL_LENGTH = 2;

	private LineNumberTableEntry[] lineNumberTable;

	/**
	 * Get the list of line number associations of the parent <tt>Code</tt>
	 * structure as an array of <tt>LineNumberTableEntry</tt> structure.
	 *
	 * @return the array
	 */
	public LineNumberTableEntry[] lineNumberTable() {
		return lineNumberTable;
	}

	/**
	 * Set the list of lineNumber number associations of the parent
	 * <tt>Code</tt> structure as an array of <tt>LineNumberTableEntry</tt>
	 * structure.
	 *
	 * @param lineNumberTable
	 *            the index
	 */
	public void setLineNumberTable(LineNumberTableEntry[] lineNumberTable) {
		this.lineNumberTable = lineNumberTable;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int lineNumberTableLength = in.readUnsignedShort();
		lineNumberTable = new LineNumberTableEntry[lineNumberTableLength];
		for (int i = 0; i < lineNumberTableLength; i++)
			lineNumberTable[i] = LineNumberTableEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int lineNumberTableLength = length(lineNumberTable);
		out.writeShort(lineNumberTableLength);
		for (int i = 0; i < lineNumberTableLength; i++)
			lineNumberTable[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return INITIAL_LENGTH + length(lineNumberTable) * LineNumberTableEntry.LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "LineNumberTable attribute with " + length(lineNumberTable) + " entries");
	}
}
