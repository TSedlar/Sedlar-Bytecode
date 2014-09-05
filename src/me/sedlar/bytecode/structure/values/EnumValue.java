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
public class EnumValue extends Value {

	public final static String ENTRY_NAME = "EnumValue";

	private static final int LENGTH = 4;
	private int typeNameIndex;
	private int constNameIndex;

	protected EnumValue() {
		super(ENUM_TAG);
	}

	/**
	 * Get the <tt>type_name_index</tt> of this element variable entry.
	 *
	 * @return the <tt>type_name_index</tt>
	 */
	public int typeNameIndex() {
		return this.typeNameIndex;
	}

	/**
	 * Set the <tt>type_name_index</tt> of this element variable entry.
	 *
	 * @param typeNameIndex
	 *            the <tt>type_name_index</tt>
	 */
	public void setTypeNameIndex(int typeNameIndex) {
		this.typeNameIndex = typeNameIndex;
	}

	/**
	 * Get the <tt>const_name_index</tt> of this element variable entry.
	 *
	 * @return the <tt>const_name_index</tt>
	 */
	public int nameIndex() {
		return this.constNameIndex;
	}

	/**
	 * Set the <tt>const_name_index</tt> of this element variable entry.
	 *
	 * @param constNameIndex
	 *            the <tt>const_name_index</tt>
	 */
	public void setNameIndex(int constNameIndex) {
		this.constNameIndex = constNameIndex;
	}

	@Override
	protected int specificLength() {
		return LENGTH;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		typeNameIndex = in.readUnsignedShort();
		constNameIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(typeNameIndex);
		out.writeShort(constNameIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "EnumValue with type_name_index " + typeNameIndex + ", const_name_index "
				+ constNameIndex);
	}

	@Override
	public String entryName() {
		return ENTRY_NAME;
	}
}
