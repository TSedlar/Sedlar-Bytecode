/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.ConstantPool;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class ConstantClassInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 2;

	private int nameIndex;

	@Override
	public byte tag() {
		return CONSTANT_CLASS;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_CLASS_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return name();
	}

	/**
	 * Get the index of the constant pool entry containing the name of the
	 * class.
	 *
	 * @return the index
	 */
	public int index() {
		return nameIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the name of the
	 * class.
	 *
	 * @param nameIndex
	 *            the index
	 */
	public void setIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	/**
	 * Get the name of the class.
	 *
	 * @return the tag
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 */
	public String name() throws InvalidByteCodeException {
		return classInfo.constantPoolUtf8Entry(nameIndex).string();
	}

	/**
	 * Set the constant pool entry string data.
	 *
	 * @param name
	 *            The data to set the constant pool entry to.
	 */
	public void setName(String name) {
		ConstantUtf8Info cpinfo;
		try {
			cpinfo = classInfo.constantPoolUtf8Entry(nameIndex);
			cpinfo.setString(name);
		} catch (InvalidByteCodeException e) {
			throw new RuntimeException("invalid constant pool index");
		}
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		nameIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_CLASS);
		out.writeShort(nameIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantClassInfo))
			return false;
		ConstantClassInfo constantClassInfo = (ConstantClassInfo) object;
		return super.equals(object) && constantClassInfo.nameIndex == nameIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ nameIndex;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with name_index " + nameIndex);
	}
}
