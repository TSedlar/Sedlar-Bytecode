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
public class SourceFileAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "SourceFile";

	private static final int LENGTH = 2;

	private int sourceFileIndex;

	/**
	 * Get the constant pool index of the name of the source file.
	 *
	 * @return the index
	 */
	public int index() {
		return sourceFileIndex;
	}

	/**
	 * Set the constant pool index of the name of the source file.
	 *
	 * @param sourceFileIndex
	 *            the index
	 */
	public void setIndex(int sourceFileIndex) {
		this.sourceFileIndex = sourceFileIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		sourceFileIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(sourceFileIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "SourceFile attribute with sourcefile_index " + sourceFileIndex);
	}
}
