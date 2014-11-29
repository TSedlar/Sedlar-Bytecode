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

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class MethodParametersEntry extends AbstractStructure {

	private int nameIndex;
	private int accessFlags;

	/**
	 * Factory method for creating <tt>StackMapFrameEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>StackMapFrameEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>StackMapFrameEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static MethodParametersEntry create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		MethodParametersEntry bootStrapMethodsEntry = new MethodParametersEntry();
		bootStrapMethodsEntry.setClassInfo(classInfo);
		bootStrapMethodsEntry.read(in);
		return bootStrapMethodsEntry;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		nameIndex = in.readUnsignedShort();
		accessFlags = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	public int nameIndex() {
		return nameIndex;
	}

	public int accessFlags() {
		return accessFlags;
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(nameIndex);
		out.writeShort(accessFlags);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "MethodParams entry");
	}

	public int length() {
		return 4;
	}

	protected String verboseAccessFlags(int accessFlags) {
		return "";
	}
}
