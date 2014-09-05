/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.values;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class ConstValue extends Value {

	public final static String ENTRY_NAME = "ConstValue";

	private static final int LENGTH = 2;
	private int constValueIndex;

	protected ConstValue(int tag) {
		super(tag);
	}

	/**
	 * Get the <tt>const_value_index</tt> of this element variable entry.
	 *
	 * @return the <tt>const_value_index</tt>
	 */
	public int valueIndex() {
		return this.constValueIndex;
	}

	/**
	 * Set the <tt>const_value_index</tt> of this element variable entry.
	 *
	 * @param constValueIndex
	 *            the <tt>const_value_index</tt>
	 */
	public void setValueIndex(int constValueIndex) {
		this.constValueIndex = constValueIndex;
	}

	@Override
	protected int specificLength() {
		return LENGTH;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		constValueIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(constValueIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "ConstValue with const_value_index " + constValueIndex);
	}

	@Override
	public String entryName() {
		return ENTRY_NAME;
	}
}
