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

import me.sedlar.bytecode.structure.AnnotationData;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class AnnotationValue extends Value implements AnnotationData {

	public final static String ENTRY_NAME = "Annotation";

	private static final int INITIAL_LENGTH = 4;

	private int typeIndex;
	private ValuePair[] valuePairEntries;

	public AnnotationValue() {
		super(ANNOTATION_TAG);
	}

	@Override
	public String entryName() {
		return ENTRY_NAME;
	}

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
	public void setPair(ValuePair[] valuePairEntries) {
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
	public void setIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		typeIndex = in.readUnsignedShort();
		int elementValuePairEntriesLength = in.readUnsignedShort();
		valuePairEntries = new ValuePair[elementValuePairEntriesLength];
		for (int i = 0; i < elementValuePairEntriesLength; i++)
			valuePairEntries[i] = ValuePair.create(in, classInfo);
		if (debug)
			debug("read ");
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
	protected int specificLength() {
		int length = INITIAL_LENGTH;
		for (ValuePair valuePairEntry : valuePairEntries) {
			length += valuePairEntry.length();
		}
		return length;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "Annotation with " + length(valuePairEntries) + " variable pair elements");
	}
}
