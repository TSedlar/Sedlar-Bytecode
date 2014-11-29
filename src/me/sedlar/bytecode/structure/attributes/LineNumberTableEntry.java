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

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class LineNumberTableEntry extends AbstractStructure {

	/**
	 * Length in bits of a lineNumber number association.
	 */
	public static final int LENGTH = 4;

	private int startPc;
	private int lineNumber;

	/**
	 * Factory method for creating <tt>LineNumberTableEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>LineNumberTableEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>LineNumberTableEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static LineNumberTableEntry create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		LineNumberTableEntry lineNumberTableEntry = new LineNumberTableEntry();
		lineNumberTableEntry.setClassInfo(classInfo);
		lineNumberTableEntry.read(in);
		return lineNumberTableEntry;
	}

	/**
	 * Get the <tt>start_pc</tt> of this lineNumber number association.
	 *
	 * @return the <tt>start_pc</tt>
	 */
	public int start() {
		return startPc;
	}

	/**
	 * Set the <tt>start_pc</tt> of this lineNumber number association.
	 *
	 * @param startPc
	 *            the <tt>start_pc</tt>
	 */
	public void setStart(int startPc) {
		this.startPc = startPc;
	}

	/**
	 * Get the lineNumber number of this lineNumber number association.
	 *
	 * @return the lineNumber number
	 */
	public int lineNumber() {
		return lineNumber;
	}

	/**
	 * Set the line number of this lineNumber number association.
	 *
	 * @param lineNumber
	 *            the lineNumber number
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		startPc = in.readUnsignedShort();
		lineNumber = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(startPc);
		out.writeShort(lineNumber);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "LineNumberTable entry with start_pc " + startPc + ", line_number " + lineNumber);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}
}
