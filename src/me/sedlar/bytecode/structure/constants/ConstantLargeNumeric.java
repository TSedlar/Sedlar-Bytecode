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
public abstract class ConstantLargeNumeric extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 8;

	/**
	 * <tt>high_bytes</tt> field.
	 */
	protected int highBytes;
	/**
	 * <tt>low_bytes</tt> field.
	 */
	protected int lowBytes;

	/**
	 * Get the <tt>high_bytes</tt> field of this constant pool entry.
	 *
	 * @return the <tt>high_bytes</tt> field
	 */
	public int high() {
		return highBytes;
	}

	/**
	 * Set the <tt>high_bytes</tt> field of this constant pool entry.
	 *
	 * @param highBytes
	 *            the <tt>high_bytes</tt> field
	 */
	public void setHigh(int highBytes) {
		this.highBytes = highBytes;
	}

	/**
	 * Get the <tt>low_bytes</tt> field of this constant pool entry.
	 *
	 * @return the <tt>low_bytes</tt> field
	 */
	public int low() {
		return lowBytes;
	}

	/**
	 * Set the <tt>low_bytes</tt> field of this constant pool entry.
	 *
	 * @param lowBytes
	 *            the <tt>low_bytes</tt> field
	 */
	public void setLow(int lowBytes) {
		this.lowBytes = lowBytes;
	}

	/**
	 * Get the the <tt>high_bytes</tt> field of this constant pool entry as a
	 * hex string.
	 *
	 * @return the hex string
	 */
	public String highFormatted() {
		return bytes(highBytes);
	}

	/**
	 * Get the the <tt>low_bytes</tt> field of this constant pool entry as a hex
	 * string.
	 *
	 * @return the hex string
	 */
	public String lowFormatted() {
		return bytes(lowBytes);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		highBytes = in.readInt();
		lowBytes = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeInt(highBytes);
		out.writeInt(lowBytes);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantLargeNumeric))
			return false;
		ConstantLargeNumeric constantLargeNumeric = (ConstantLargeNumeric) object;
		return super.equals(object) && constantLargeNumeric.highBytes == highBytes
				&& constantLargeNumeric.lowBytes == lowBytes;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ highBytes ^ lowBytes;
	}
}
