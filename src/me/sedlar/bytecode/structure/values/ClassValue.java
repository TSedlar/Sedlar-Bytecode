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
public class ClassValue extends Value {

	public final static String ENTRY_NAME = "ClassValue";

	private static final int LENGTH = 2;
	private int classInfoIndex;

	protected ClassValue() {
		super(CLASS_TAG);
	}

	/**
	 * Get the <tt>class_info_index</tt> of this element variable entry.
	 *
	 * @return the <tt>class_info_index</tt>
	 */
	public int classIndex() {
		return this.classInfoIndex;
	}

	/**
	 * Set the <tt>class_info_index</tt> of this element variable entry.
	 *
	 * @param classInfoIndex
	 *            the <tt>class_info_index</tt>
	 */
	public void setCLassIndex(int classInfoIndex) {
		this.classInfoIndex = classInfoIndex;
	}

	@Override
	protected int specificLength() {
		return LENGTH;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		classInfoIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(classInfoIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "ClassValue with class_info_index " + classInfoIndex);
	}

	@Override
	public String entryName() {
		return ENTRY_NAME;
	}
}
