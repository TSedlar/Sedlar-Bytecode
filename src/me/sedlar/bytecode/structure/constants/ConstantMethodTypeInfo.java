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
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ConstantMethodTypeInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 2;

	private int descriptorIndex;

	@Override
	public byte tag() {
		return CONSTANT_METHOD_TYPE;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_METHOD_TYPE_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return name();
	}

	/**
	 * Get the index of the constant pool entry containing the descriptor of the
	 * method.
	 *
	 * @return the index
	 */
	public int descriptorIndex() {
		return descriptorIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the descriptor of the
	 * method.
	 *
	 * @param descriptorIndex
	 *            the index
	 */
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	/**
	 * Get the descriptor.
	 *
	 * @return the descriptor
	 * @throws me.sedlar.bytecode.structure.InvalidByteCodeException
	 *             if the byte code is invalid
	 */
	public String name() throws InvalidByteCodeException {
		return classInfo.constantPoolUtf8Entry(descriptorIndex).string();
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		descriptorIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_METHOD_TYPE);
		out.writeShort(descriptorIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantMethodTypeInfo))
			return false;
		ConstantMethodTypeInfo constantMethodTypeInfo = (ConstantMethodTypeInfo) object;
		return super.equals(object) && constantMethodTypeInfo.descriptorIndex == descriptorIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ descriptorIndex;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with descriptor_index " + descriptorIndex);
	}
}
