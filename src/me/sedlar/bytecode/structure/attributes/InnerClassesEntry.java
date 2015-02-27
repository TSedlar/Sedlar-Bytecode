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
import me.sedlar.bytecode.structure.AccessFlag;
import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class InnerClassesEntry extends AbstractStructure {

	/**
	 * Length in bits of an inner class entry.
	 */
	public static final int LENGTH = 8;

	private int innerClassInfoIndex;
	private int outerClassInfoIndex;
	private int innerNameIndex;
	private int innerClassAccessFlags;

	/**
	 * Factory method for creating <tt>InnerClassesEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>InnerClassesEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>InnerClassesEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static InnerClassesEntry create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		InnerClassesEntry innerClassesEntry = new InnerClassesEntry();
		innerClassesEntry.setClassInfo(classInfo);
		innerClassesEntry.read(in);
		return innerClassesEntry;
	}

	/**
	 * Get the constant pool index of the <tt>CONSTANT_Class_info</tt> structure
	 * describing the inner class of this <tt>InnerClassEntry</tt>.
	 *
	 * @return the index
	 */
	public int innerClassIndex() {
		return innerClassInfoIndex;
	}

	/**
	 * Set the constant pool index of the <tt>CONSTANT_Class_info</tt> structure
	 * describing the inner class of this <tt>InnerClassEntry</tt>.
	 *
	 * @param innerClassInfoIndex
	 *            the index
	 */
	public void setInnerClassIndex(int innerClassInfoIndex) {
		this.innerClassInfoIndex = innerClassInfoIndex;
	}

	/**
	 * Get the constant pool index of the <tt>CONSTANT_Class_info</tt> structure
	 * describing the outer class of this <tt>InnerClassEntry</tt>.
	 *
	 * @return the index
	 */
	public int outerClassIndex() {
		return outerClassInfoIndex;
	}

	/**
	 * Set the constant pool index of the <tt>CONSTANT_Class_info</tt> structure
	 * describing the outer class of this <tt>InnerClassEntry</tt>.
	 *
	 * @param outerClassInfoIndex
	 *            the index
	 */
	public void setOuterClassIndex(int outerClassInfoIndex) {
		this.outerClassInfoIndex = outerClassInfoIndex;
	}

	/**
	 * Get the constant pool index containing the simple name of the inner class
	 * of this <tt>InnerClassEntry</tt>.
	 *
	 * @return the index
	 */
	public int innerNameIndex() {
		return innerNameIndex;
	}

	/**
	 * Set the constant pool index containing the simple name of the inner class
	 * of this <tt>InnerClassEntry</tt>.
	 *
	 * @param innerNameIndex
	 *            the index
	 */
	public void setInnerNameIndex(int innerNameIndex) {
		this.innerNameIndex = innerNameIndex;
	}

	/**
	 * Get the accessFlags flags of the inner class.
	 *
	 * @return the accessFlags flags
	 */
	public int innerAccessFlags() {
		return innerClassAccessFlags;
	}

	/**
	 * Set the accessFlags flags of the inner class.
	 *
	 * @param innerClassAccessFlags
	 *            the accessFlags flags
	 */
	public void setInnerClassAccessFlags(int innerClassAccessFlags) {
		this.innerClassAccessFlags = innerClassAccessFlags;
	}

	/**
	 * Get the the accessFlags flags of the inner class as a hex string.
	 *
	 * @return the hex string
	 */
	public String formattedInnerAccessFlags() {
		return accessFlags(innerClassAccessFlags);
	}

	/**
	 * Get the verbose description of the accessFlags flags of the inner class.
	 *
	 * @return the description
	 */
	public String verboseInnerAcessFlags() {
		return verboseAccessFlags(innerClassAccessFlags);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		innerClassInfoIndex = in.readUnsignedShort();
		outerClassInfoIndex = in.readUnsignedShort();
		innerNameIndex = in.readUnsignedShort();
		innerClassAccessFlags = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(innerClassInfoIndex);
		out.writeShort(outerClassInfoIndex);
		out.writeShort(innerNameIndex);
		out.writeShort(innerClassAccessFlags);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "InnerClasses entry with inner_class_info_index " + innerClassInfoIndex
				+ ", outer_class_info_index " + outerClassInfoIndex + ", inner_name_index " + innerNameIndex
				+ ", accessFlags flags " + accessFlags(innerClassAccessFlags));
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return verboseAccessFlags(AccessFlag.INNER_CLASS_ACCESS_FLAGS, accessFlags);
	}
}
