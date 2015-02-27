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
public class ConstantNameAndTypeInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 4;

	private int nameIndex;
	private int descriptorIndex;

	@Override
	public byte tag() {
		return CONSTANT_NAME_AND_TYPE;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_NAME_AND_TYPE_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return name() + descriptor();
	}

	/**
	 * Get the index of the constant pool entry containing the name of this
	 * entry.
	 *
	 * @return the index
	 */
	public int nameIndex() {
		return nameIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the name of this
	 * entry.
	 *
	 * @param nameIndex
	 *            the index
	 */
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	/**
	 * Get the index of the constant pool entry containing the descriptor of
	 * this entry.
	 *
	 * @return the index
	 */
	public int descriptorIndex() {
		return descriptorIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the descriptor of
	 * this entry.
	 *
	 * @param descriptorIndex
	 *            the index
	 */
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	/**
	 * Get the name.
	 *
	 * @return the name.
	 * @throws InvalidByteCodeException
	 */
	public String name() throws InvalidByteCodeException {
		return classInfo.constantPoolEntryName(nameIndex);
	}

	/**
	 * Set the name.
	 *
	 * @param name the name.
	 */
	public void setName(String name) {
		ConstantUtf8Info info = (ConstantUtf8Info) classInfo.constantPoolAt(nameIndex);
		info.setString(name);
	}

	/**
	 * Get the descriptor string.
	 *
	 * @return the string.
	 * @throws InvalidByteCodeException
	 */
	public String descriptor() throws InvalidByteCodeException {
		return classInfo.constantPoolEntryName(descriptorIndex);
	}

	/**
	 * Set the descriptor.
	 *
	 * @param descriptor the descriptor.
	 */
	public void setDescriptor(String descriptor) {
		ConstantUtf8Info info = (ConstantUtf8Info) classInfo.constantPoolAt(descriptorIndex);
		info.setString(descriptor);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		nameIndex = in.readUnsignedShort();
		descriptorIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_NAME_AND_TYPE);
		out.writeShort(nameIndex);
		out.writeShort(descriptorIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with name_index " + nameIndex + " and descriptor_index "
				+ descriptorIndex);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantNameAndTypeInfo))
			return false;
		ConstantNameAndTypeInfo constantNameAndTypeInfo = (ConstantNameAndTypeInfo) object;
		return super.equals(object) && constantNameAndTypeInfo.nameIndex == nameIndex
				&& constantNameAndTypeInfo.descriptorIndex == descriptorIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ nameIndex ^ descriptorIndex;
	}
}
