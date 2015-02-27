/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.values.ValuePair;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class Annotation extends AbstractStructure implements AnnotationData {

	private static final int INITIAL_LENGTH = 4;

	private int typeIndex;
	private ValuePair[] valuePairEntries;

	public ValuePair[] pair() {
		return valuePairEntries;
	}

	/**
	 * Set the list of element variable pair associations of the parent
	 * structure as an array of <tt>ValuePair</tt> structure.
	 *
	 * @param valuePairEntries
	 *            the array
	 */
	public void setValuePairEntries(ValuePair[] valuePairEntries) {
		this.valuePairEntries = valuePairEntries;
	}

	public int index() {
		return typeIndex;
	}

	/**
	 * Set the <tt>type_index</tt> of this annotation.
	 *
	 * @param typeIndex
	 *            the <tt>type_index</tt>
	 */
	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);

		typeIndex = in.readUnsignedShort();
		int elementValuePairEntriesLength = in.readUnsignedShort();

		valuePairEntries = new ValuePair[elementValuePairEntriesLength];

		for (int i = 0; i < elementValuePairEntriesLength; i++) {
			valuePairEntries[i] = ValuePair.create(in, classInfo);
		}

		if (debug)
			debug("read ");
	}

	public int length() {
		int length = INITIAL_LENGTH;
		for (ValuePair valuePairEntry : valuePairEntries)
			length += valuePairEntry.length();
		return length;
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(typeIndex);
		int elementValuePairEntriesLength = length(valuePairEntries);
		out.writeShort(elementValuePairEntriesLength);
		for (int i = 0; i < elementValuePairEntriesLength; i++)
			valuePairEntries[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "Annotation with " + length(valuePairEntries) + " value pair elements");
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}
}
