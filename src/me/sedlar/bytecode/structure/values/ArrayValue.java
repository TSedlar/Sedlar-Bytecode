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
public class ArrayValue extends Value {

	public final static String ENTRY_NAME = "ArrayValue";

	private static final int INITIAL_LENGTH = 2;
	private Value[] valueEntries;

	protected ArrayValue() {
		super(ARRAY_TAG);
	}

	/**
	 * Get the list of element values associations of the this array element
	 * variable entry.
	 *
	 * @return the array
	 */
	public Value[] entries() {
		return this.valueEntries;
	}

	/**
	 * Set the list of element values associations of this array element
	 * variable entry.
	 *
	 * @param valueEntries
	 *            the array
	 */
	public void setEntries(Value[] valueEntries) {
		this.valueEntries = valueEntries;
	}

	@Override
	protected int specificLength() {
		int length = INITIAL_LENGTH;
		for (Value valueEntry : valueEntries)
			length += valueEntry.length();
		return length;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		int elementValueEntriesLength = in.readUnsignedShort();
		valueEntries = new Value[elementValueEntriesLength];
		for (int i = 0; i < valueEntries.length; i++)
			valueEntries[i] = create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int elementValueEntriesLength = length(valueEntries);
		out.writeShort(elementValueEntriesLength);
		for (int i = 0; i < elementValueEntriesLength; i++)
			valueEntries[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "ArrayValue with " + length(valueEntries) + " entries");
	}

	@Override
	public String entryName() {
		return ENTRY_NAME;
	}
}
