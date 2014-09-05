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

import me.sedlar.bytecode.structure.AttributeInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class MethodParametersAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "MethodParameters";

	private static final int INITIAL_LENGTH = 1;

	private MethodParametersEntry[] entries;

	/**
	 * Get the list of stackMapFrame entries in the
	 * <tt>StackMapTableAttribute</tt> structure as an array of
	 * <tt>BootstrapMethodsEntry</tt> structure.
	 *
	 * @return the array
	 */
	public MethodParametersEntry[] entries() {
		return entries;
	}

	/**
	 * Set the list of stackMapFrame entries in the
	 * <tt>StackMapTableAttribute</tt> structure as an array of
	 * <tt>StackMapFrameEntry</tt> structure.
	 *
	 * @param entries
	 *            the array
	 */
	public void setEntries(MethodParametersEntry[] entries) {
		this.entries = entries;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int numberOfEntries = in.readUnsignedByte();
		entries = new MethodParametersEntry[numberOfEntries];
		for (int i = 0; i < numberOfEntries; i++)
			entries[i] = MethodParametersEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int numberOfRefs = length(entries);
		out.writeShort(numberOfRefs);
		for (int i = 0; i < numberOfRefs; i++)
			entries[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		int size = INITIAL_LENGTH;
		for (MethodParametersEntry entry : entries)
			size += entry.length();
		return size;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "MethodParametersEntry attribute with " + length(entries) + " entries");
	}
}
