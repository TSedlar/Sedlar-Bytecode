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
public abstract class ConstantReference extends ConstantPool {

	/**
	 * Length of the constant pool data structure in bits.
	 */
	public static final int SIZE = 4;

	/**
	 * <tt>class_index</tt> field.
	 */
	protected int classIndex;
	/**
	 * <tt>name_and_type_index</tt> field.
	 */
	protected int nameAndTypeIndex;

	@Override
	public String verbose() throws InvalidByteCodeException {
		ConstantNameAndTypeInfo nameAndType = nameAndTypeInfo();
		return classInfo.constantPoolEntryName(classIndex) + "."
				+ classInfo.constantPoolEntryName(nameAndType.nameIndex());
	}

	/**
	 * Get the index of the constant pool entry containing the
	 * <tt>CONSTANT_Class_info</tt> of this entry.
	 *
	 * @return the index
	 */
	public int classIndex() {
		return classIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the
	 * <tt>CONSTANT_Class_info</tt> of this entry.
	 *
	 * @param classIndex
	 *            the index
	 */
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

	/**
	 * Get the index of the constant pool entry containing the
	 * <tt>CONSTANT_NameAndType_info</tt> of this entry.
	 *
	 * @return the index
	 */
	public int nameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the
	 * <tt>CONSTANT_NameAndType_info</tt> of this entry.
	 *
	 * @param nameAndTypeIndex
	 *            the index
	 */
	public void setNameAndTypeIndex(int nameAndTypeIndex) {
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	/**
	 * Get the class info for this reference.
	 *
	 * @return the class info.
	 * @throws InvalidByteCodeException
	 */
	public ConstantClassInfo constantClassInfo() throws InvalidByteCodeException {
		return (ConstantClassInfo) classInfo().constantPoolAt(classIndex, ConstantClassInfo.class);
	}

	/**
	 * Get the name and type info for this reference.
	 *
	 * @return the name and type info.
	 * @throws InvalidByteCodeException
	 */
	public ConstantNameAndTypeInfo nameAndTypeInfo() throws InvalidByteCodeException {
		return (ConstantNameAndTypeInfo) classInfo.constantPoolAt(nameAndTypeIndex, ConstantNameAndTypeInfo.class);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		classIndex = in.readUnsignedShort();
		nameAndTypeIndex = in.readUnsignedShort();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeShort(classIndex);
		out.writeShort(nameAndTypeIndex);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantReference))
			return false;
		ConstantReference constantReference = (ConstantReference) object;
		return super.equals(object) && constantReference.classIndex == classIndex
				&& constantReference.nameAndTypeIndex == nameAndTypeIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ classIndex ^ nameAndTypeIndex;
	}
}
