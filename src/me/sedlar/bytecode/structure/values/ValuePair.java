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

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class ValuePair extends AbstractStructure {

	public final static String ENTRY_NAME = "ValuePair";

	private static final int INITIAL_LENGTH = 2;

	private int elementNameIndex;
	private Value value;

	/**
	 * Factory for creating <tt>ValuePair</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>ValuePair</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>Value</tt> structure
	 * @throws me.sedlar.bytecode.structure.InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws java.io.IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static ValuePair create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException, IOException {
		ValuePair valuePairEntry = new ValuePair();
		valuePairEntry.setClassInfo(classInfo);
		valuePairEntry.read(in);
		return valuePairEntry;
	}

	/**
	 * Get the <tt>element_value</tt> of this element variable pair.
	 *
	 * @return the <tt>element_value</tt>
	 */
	public Value value() {
		return this.value;
	}

	/**
	 * Set the <tt>element_value</tt> of this element variable pair.
	 *
	 * @param value
	 *            the <tt>element_value</tt>
	 */
	public void setValue(Value value) {
		this.value = value;
	}

	/**
	 * Get the <tt>element_name_index</tt> of this element variable pair.
	 *
	 * @return the <tt>element_name_index</tt>
	 */
	public int nameIndex() {
		return elementNameIndex;
	}

	/**
	 * Set the <tt>element_name_index</tt> of this element variable pair.
	 *
	 * @param elementNameIndex
	 *            the <tt>element_name_index</tt>
	 */
	public void setNameIndex(int elementNameIndex) {
		this.elementNameIndex = elementNameIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		elementNameIndex = in.readUnsignedShort();
		value = Value.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(elementNameIndex);
		value.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}

	public int length() {
		return INITIAL_LENGTH + value.length();
	}

	public String entryName() {
		return ENTRY_NAME;
	}
}
