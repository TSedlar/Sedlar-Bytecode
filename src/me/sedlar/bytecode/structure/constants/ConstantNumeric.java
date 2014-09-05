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
public abstract class ConstantNumeric extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 4;

	/**
	 * <tt>bits</tt> field.
	 */
	protected int bits;

	/**
	 * Get the <tt>bits</tt> field of this constant pool entry.
	 *
	 * @return the <tt>bits</tt> field
	 */
	public int bits() {
		return bits;
	}

	/**
	 * Set the <tt>bits</tt> field of this constant pool entry.
	 *
	 * @param bits
	 *            the <tt>bits</tt> field
	 */
	public void setBits(int bits) {
		this.bits = bits;
	}

	/**
	 * Get the the <tt>bits</tt> field of this constant pool entry as a hex
	 * string.
	 *
	 * @return the hex string
	 */
	public String formattedBits() {
		return bytes(bits);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		bits = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeInt(bits);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantNumeric))
			return false;
		ConstantNumeric constantNumeric = (ConstantNumeric) object;
		return super.equals(object) && constantNumeric.bits == bits;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ bits;
	}
}
