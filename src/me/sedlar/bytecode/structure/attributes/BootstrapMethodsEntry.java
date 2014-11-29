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
public class BootstrapMethodsEntry extends AbstractStructure {

	private int methodRefIndex;
	private int argumentIndices[];

	/**
	 * Factory method for creating <tt>BootstrapMethodsEntry</tt> structure.
	 *
	 * @param in
	 *            the <tt>DataInput</tt> from which to read the
	 *            <tt>BootstrapMethodsEntry</tt> structure
	 * @param classInfo
	 *            the parent class file of the structure to be created
	 * @return the new <tt>BootstrapMethodsEntry</tt> structure
	 * @throws InvalidByteCodeException
	 *             if the byte code is invalid
	 * @throws IOException
	 *             if an exception occurs with the <tt>DataInput</tt>
	 */
	public static BootstrapMethodsEntry create(DataInput in, ClassInfo classInfo) throws InvalidByteCodeException,
			IOException {
		BootstrapMethodsEntry bootStrapMethodsEntry = new BootstrapMethodsEntry();
		bootStrapMethodsEntry.setClassInfo(classInfo);
		bootStrapMethodsEntry.read(in);
		return bootStrapMethodsEntry;
	}

	/**
	 * Get the constant pool index of the <tt>CONSTANT_MethodRef_info</tt>
	 * structure describing the bootstrap method of this
	 * <tt>BootstrapMethodsEntry</tt>.
	 *
	 * @return the index
	 */
	public int methodRefIndex() {
		return methodRefIndex;
	}

	/**
	 * Set the constant pool index of the <tt>CONSTANT_MethodRef_info</tt>
	 * structure describing the bootstrap method of this
	 * <tt>BootstrapMethodsEntry</tt>.
	 *
	 * @param methodRefIndex
	 *            the index
	 */
	public void setMethodRefIndex(int methodRefIndex) {
		this.methodRefIndex = methodRefIndex;
	}

	/**
	 * Get the array of argument references of this
	 * <tt>BootstrapMethodsEntry</tt>.
	 *
	 * @return the argument references
	 */
	public int[] argumentIndices() {
		return argumentIndices;
	}

	/**
	 * Set the array of argument references of this
	 * <tt>BootstrapMethodsEntry</tt>.
	 *
	 * @param argumentIndices
	 *            the argument references
	 */
	public void setArgumentIndices(int argumentIndices[]) {
		this.argumentIndices = argumentIndices;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		methodRefIndex = in.readUnsignedShort();
		int argumentRefsCount = in.readUnsignedShort();
		argumentIndices = new int[argumentRefsCount];
		for (int i = 0; i < argumentRefsCount; i++)
			argumentIndices[i] = in.readUnsignedShort();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(methodRefIndex);
		int argumentRefsCount = length(argumentIndices);
		out.writeShort(argumentRefsCount);
		for (int i = 0; i < argumentRefsCount; i++)
			out.writeShort(argumentIndices[i]);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "BootstrapMethods entry with bootstrap_method_index " + methodRefIndex
				+ ", arguments (" + verbose() + ")");
	}

	public String verbose() {
		StringBuilder buffer = new StringBuilder();
		int argumentRefsCount = length(argumentIndices);
		for (int i = 0; i < argumentRefsCount; i++) {
			if (i > 0)
				buffer.append("\n");
			int argumentIndex = argumentIndices[i];
			buffer.append("<a href=\"").append(argumentIndex).append("\">cp_info #").append(argumentIndex).append(
					"</a> &lt;").append(verboseIndex(argumentIndex)).append("&gt;");
		}
		return buffer.toString();
	}

	private String verboseIndex(int index) {
		try {
			return classInfo().constantPoolEntryName(index);
		} catch (InvalidByteCodeException e) {
			return "invalid constant pool index " + index;
		}
	}

	public int length() {
		return 4 + length(argumentIndices) * 2;
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return null;
	}
}
