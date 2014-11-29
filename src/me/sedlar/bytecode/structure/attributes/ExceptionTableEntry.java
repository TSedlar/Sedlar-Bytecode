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
public class ExceptionTableEntry extends AbstractStructure {

	/**
	 * Length in bits of an exception table entry.
	 */
	public static final int LENGTH = 8;

	private int startPc;
	private int endPc;
	private int handlerPc;
	private int catchType;

	/**
	 * Factory method for creating <tt>ExceptionTableEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>ExceptionTableEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>ExceptionTableEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static ExceptionTableEntry create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		ExceptionTableEntry exceptionTableEntry = new ExceptionTableEntry();
		exceptionTableEntry.setClassInfo(classInfo);
		exceptionTableEntry.read(in);
		return exceptionTableEntry;
	}

	/**
	 * Constructor.
	 */
	public ExceptionTableEntry() {
	}

	/**
	 * Constructor.
	 *
	 * @param startPc
	 *            the <tt>start_pc</tt>
	 * @param endPc
	 *            the <tt>end_pc</tt>
	 * @param handlerPc
	 *            the <tt>handler_pc</tt>
	 * @param catchType
	 *            the constant pool index for the catch type of this exception
	 *            table entry
	 */
	public ExceptionTableEntry(int startPc, int endPc, int handlerPc, int catchType) {
		this.startPc = startPc;
		this.endPc = endPc;
		this.handlerPc = handlerPc;
		this.catchType = catchType;
	}

	/**
	 * Get the <tt>start_pc</tt> of this exception table entry.
	 *
	 * @return the <tt>start_pc</tt>
	 */
	public int start() {
		return startPc;
	}

	/**
	 * Set the <tt>start_pc</tt> of this exception table entry.
	 *
	 * @param startPc
	 *            the <tt>start_pc</tt>
	 */
	public void setStart(int startPc) {
		this.startPc = startPc;
	}

	/**
	 * Get the <tt>end_pc</tt> of this exception table entry.
	 *
	 * @return the <tt>end_pc</tt>
	 */
	public int end() {
		return endPc;
	}

	/**
	 * Set the <tt>end_pc</tt> of this exception table entry.
	 *
	 * @param endPc
	 *            the <tt>end_pc</tt>
	 */
	public void setEnd(int endPc) {
		this.endPc = endPc;
	}

	/**
	 * Get the <tt>handler_pc</tt> of this exception table entry.
	 *
	 * @return the <tt>handler_pc</tt>
	 */
	public int handler() {
		return handlerPc;
	}

	/**
	 * Set the <tt>handler_pc</tt> of this exception table entry.
	 *
	 * @param handlerPc
	 *            the <tt>handler_pc</tt>
	 */
	public void setHandler(int handlerPc) {
		this.handlerPc = handlerPc;
	}

	/**
	 * Get the constant pool index for the catch type of this exception table
	 * entry.
	 *
	 * @return the index
	 */
	public int catchType() {
		return catchType;
	}

	/**
	 * Set the constant pool index for the catch type of this exception table
	 * entry.
	 *
	 * @param catchType
	 *            the index
	 */
	public void setCatchType(int catchType) {
		this.catchType = catchType;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		startPc = in.readUnsignedShort();
		endPc = in.readUnsignedShort();
		handlerPc = in.readUnsignedShort();
		catchType = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(startPc);
		out.writeShort(endPc);
		out.writeShort(handlerPc);
		out.writeShort(catchType);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "exception table entry with start_pc " + startPc + ", end_pc " + endPc
				+ ", handler_pc " + handlerPc + ", catch_type index " + catchType);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}
}
