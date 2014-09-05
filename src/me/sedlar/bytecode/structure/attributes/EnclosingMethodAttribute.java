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
public class EnclosingMethodAttribute extends AttributeInfo {
	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "EnclosingMethod";

	private static final int LENGTH = 4;

	private int classInfoIndex;
	private int methodInfoIndex;

	/**
	 * Get the constant pool index of the <tt>CONSTANT_Class_info</tt> structure
	 * representing the innermost class that encloses the declaration of the
	 * current class.
	 *
	 * @return the index
	 */
	public int classIndex() {
		return classInfoIndex;
	}

	/**
	 * Get the constant pool index of the <tt>CONSTANT_NameAndType_info</tt>
	 * structure representing the name and type of a method in the class
	 * referenced by the class info index above.
	 *
	 * @return the index
	 */
	public int methodIndex() {
		return methodInfoIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		classInfoIndex = in.readUnsignedShort();
		methodInfoIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(classInfoIndex);
		out.writeShort(methodInfoIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		return LENGTH;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "EnclosingMethod attribute with class index " + classInfoIndex
				+ " and method index " + methodInfoIndex);
	}
}
