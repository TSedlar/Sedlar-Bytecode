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
public class VerificationTypeInfoEntry extends AbstractStructure {

	/**
	 * Factory method for creating <tt>VerificationTypeInfoEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>VerificationTypeInfoEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>VerificationTypeInfoEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static VerificationTypeInfoEntry create(DataInput in, ClassInfo classInfo)
			throws InvalidByteCodeException, IOException {
		int tag = in.readUnsignedByte();
		VerificationType verificationType = VerificationType.getFromTag(tag);
		VerificationTypeInfoEntry entry = verificationType.createEntry();
		entry.setClassInfo(classInfo);
		entry.read(in);
		return entry;
	}

	private VerificationType type;

	public VerificationTypeInfoEntry(VerificationType type) {
		this.type = type;
	}

	/**
	 * Returns the verification type
	 */
	public VerificationType type() {
		return type;
	}

	@Override
	public final void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		readExtra(in);
		if (debug)
			debug("read ");
	}

	/**
	 * Read extra data in derived classes.
	 */
	protected void readExtra(DataInput in) throws InvalidByteCodeException, IOException {

	}

	@Override
	public final void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(type.tag());
		writeExtra(out);
		if (debug)
			debug("wrote ");
	}

	/**
	 * Write extra data in derived classes.
	 */
	protected void writeExtra(DataOutput out) throws InvalidByteCodeException, IOException {

	}

	@Override
	protected void debug(String message) {
		super.debug(message + "VerificationTypeInfo entry of type " + type);
	}

	/**
	 * Returns the bytecode length of the entry.
	 */
	public int length() {
		return 1;
	}

	/**
	 * Append the verbose representation to a string builder.
	 */
	public void appendTo(StringBuilder buffer) {
		buffer.append(type);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return null;
	}
}
