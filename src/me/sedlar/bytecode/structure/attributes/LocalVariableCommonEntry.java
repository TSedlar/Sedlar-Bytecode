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
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class LocalVariableCommonEntry extends AbstractStructure {

	/**
	 * Length in bits of a local variable association.
	 */
	public static final int LENGTH = 10;

	protected int startPc;
	protected int length;
	protected int nameIndex;
	protected int descriptorOrSignatureIndex;
	protected int index;

	/**
	 * Get the <tt>start_pc</tt> of this local variable association.
	 *
	 * @return the <tt>start_pc</tt>
	 */
	public int start() {
		return startPc;
	}

	/**
	 * Set the <tt>start_pc</tt> of this local variable association.
	 *
	 * @param startPc
	 *            the <tt>start_pc</tt>
	 */
	public void setStart(int startPc) {
		this.startPc = startPc;
	}

	/**
	 * Get the length in bits of this local variable association.
	 *
	 * @return the length
	 */
	public int length() {
		return length;
	}

	/**
	 * Set the length in bits of this local variable association.
	 *
	 * @param length
	 *            the length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Get the index of the constant pool entry containing the name of this
	 * local variable.
	 *
	 * @return the index
	 */
	public int nameIndex() {
		return nameIndex;
	}

	/**
	 * Set the index of the constant pool entry containing the name of this
	 * local variable.
	 *
	 * @param nameIndex
	 *            the index
	 */
	public void setNameIndex(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	/**
	 * Get the index of the constant pool entry containing the descriptor of
	 * this local variable.
	 *
	 * @return the index
	 */
	public int descriptorIndex() {
		return descriptorOrSignatureIndex;
	}

	/**
	 * Get the index of the constant pool entry containing the descriptor of
	 * this local variable.
	 *
	 * @param descriptorIndex
	 *            the index
	 */
	public void setDescriptorIndex(int descriptorIndex) {
		this.descriptorOrSignatureIndex = descriptorIndex;
	}

	/**
	 * Get the index of this local variable.
	 *
	 * @return the index
	 */
	public int index() {
		return index;
	}

	/**
	 * Set the index of this local variable. Set the index of this local
	 * variable.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		startPc = in.readUnsignedShort();
		length = in.readUnsignedShort();
		nameIndex = in.readUnsignedShort();
		descriptorOrSignatureIndex = in.readUnsignedShort();
		index = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(startPc);
		out.writeShort(length);
		out.writeShort(nameIndex);
		out.writeShort(descriptorOrSignatureIndex);
		out.writeShort(index);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		if (accessFlags != 0)
			throw new RuntimeException("Access flags should be zero: " + Integer.toHexString(accessFlags));
		return "";
	}
}
