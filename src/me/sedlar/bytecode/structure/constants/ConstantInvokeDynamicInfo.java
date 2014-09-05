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
public class ConstantInvokeDynamicInfo extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 4;

	private int bootstrapMethodAttributeIndex;
	private int nameAndTypeIndex;

	@Override
	public byte tag() {
		return CONSTANT_INVOKE_DYNAMIC;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_INVOKE_DYNAMIC_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		ConstantNameAndTypeInfo nameAndType = nameAndTypeInfo();
		return nameAndType.name() + ", BootstrapMethods #" + bootstrapMethodAttributeIndex;
	}

	public int bootstrapIndex() {
		return bootstrapMethodAttributeIndex;
	}

	public void setBootstrapIndex(int bootstrapMethodAttributeIndex) {
		this.bootstrapMethodAttributeIndex = bootstrapMethodAttributeIndex;
	}

	public int nameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	public void setNameAndTypeIndex(int nameAndTypeIndex) {
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public ConstantNameAndTypeInfo nameAndTypeInfo() throws InvalidByteCodeException {
		return (ConstantNameAndTypeInfo) classInfo.constantPoolAt(nameAndTypeIndex, ConstantNameAndTypeInfo.class);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		bootstrapMethodAttributeIndex = in.readUnsignedShort();
		nameAndTypeIndex = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_INVOKE_DYNAMIC);
		out.writeShort(bootstrapMethodAttributeIndex);
		out.writeShort(nameAndTypeIndex);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with bootstrap method attr index " + bootstrapMethodAttributeIndex
				+ " and name and type index " + nameAndTypeIndex);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantInvokeDynamicInfo))
			return false;
		ConstantInvokeDynamicInfo constantNameAndTypeInfo = (ConstantInvokeDynamicInfo) object;
		return super.equals(object)
				&& constantNameAndTypeInfo.bootstrapMethodAttributeIndex == bootstrapMethodAttributeIndex
				&& constantNameAndTypeInfo.nameAndTypeIndex == nameAndTypeIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ bootstrapMethodAttributeIndex ^ nameAndTypeIndex;
	}
}
