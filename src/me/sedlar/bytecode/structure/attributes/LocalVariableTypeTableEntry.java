/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.IOException;

import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class LocalVariableTypeTableEntry extends LocalVariableCommonEntry {
	/**
	 * Factory method for creating <tt>LocalVariableTypeTableEntry</tt>
	 * structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>LocalVariableTypeTableEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>LocalVariableTypeTableEntry</tt> structure
	 * @throws me.sedlar.bytecode.structure.InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws java.io.IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static LocalVariableTypeTableEntry create(DataInput in, ClassInfo classInfo)
			throws InvalidByteCodeException, IOException {
		LocalVariableTypeTableEntry localVariableTypeTableEntry = new LocalVariableTypeTableEntry();
		localVariableTypeTableEntry.setClassInfo(classInfo);
		localVariableTypeTableEntry.read(in);
		return localVariableTypeTableEntry;
	}

	/**
	 * Get the index of the constant pool entry containing the signature of this
	 * local variable.
	 *
	 * @return the index
	 */
	@Override
	public int descriptorIndex() {
		return descriptorOrSignatureIndex;
	}

	/**
	 * Get the index of the constant pool entry containing the signature of this
	 * local variable.
	 *
	 * @param signatureIndex
	 *            the index
	 */
	@Override
	public void setDescriptorIndex(int signatureIndex) {
		this.descriptorOrSignatureIndex = signatureIndex;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "LocalVariableTypeTable entry with start_pc " + startPc + ", length " + length
				+ ", name_index " + nameIndex + ", signature_index " + descriptorOrSignatureIndex + ", index "
				+ index);
	}
}
