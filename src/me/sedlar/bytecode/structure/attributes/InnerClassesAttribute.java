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
public class InnerClassesAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "InnerClasses";

	private static final int INITIAL_LENGTH = 2;

	private InnerClassesEntry[] classes;

	/**
	 * Get the list of inner classes of the parent <tt>ClassInfo</tt> structure
	 * as an array of <tt>InnerClassesEntry</tt> structure.
	 *
	 * @return the array
	 */
	public InnerClassesEntry[] classes() {
		return classes;
	}

	/**
	 * Set the list of inner classes of the parent <tt>ClassInfo</tt> structure
	 * as an array of <tt>InnerClassesEntry</tt> structure.
	 *
	 * @param classes
	 *            the array
	 */
	public void setClasses(InnerClassesEntry[] classes) {
		this.classes = classes;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int numberOfClasses = in.readUnsignedShort();
		classes = new InnerClassesEntry[numberOfClasses];
		for (int i = 0; i < numberOfClasses; i++)
			classes[i] = InnerClassesEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int numberOfClasses = length(classes);
		out.writeShort(numberOfClasses);
		for (int i = 0; i < numberOfClasses; i++)
			classes[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return INITIAL_LENGTH + length(classes) * InnerClassesEntry.LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "InnerClasses attribute with " + length(classes) + " classes");
	}
}
