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

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class FieldInfo extends ClassMember {

	/**
	 * Factory method for creating <tt>FieldInfo</tt> structure from a
	 * <tt>DataInput</tt>.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>FieldInfo</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>FieldInfo</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static FieldInfo create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException, IOException {
		FieldInfo fieldInfo = new FieldInfo();
		fieldInfo.setClassInfo(classInfo);
		fieldInfo.read(in);
		return fieldInfo;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "field with access flags " + accessFlags(accessFlags) + ", name_index " + nameIndex
				+ ", descriptor_index " + descriptorIndex + ", " + length(attributes) + " attributes");
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return verboseAccessFlags(AccessFlag.FIELD_ACCESS_FLAGS, accessFlags);
	}
}
