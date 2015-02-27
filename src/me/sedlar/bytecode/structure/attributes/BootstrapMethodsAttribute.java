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

import me.sedlar.bytecode.structure.AttributeInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BootstrapMethodsAttribute extends AttributeInfo {

	/**
	 * Name of the attribute as in the corresponding constant pool entry.
	 */
	public static final String ATTRIBUTE_NAME = "BootstrapMethods";

	private static final int INITIAL_LENGTH = 2;

	private BootstrapMethodsEntry[] methods;

	/**
	 * Get the list of bootstrap method references in the
	 * <tt>BootstrapMethodsAttribute</tt> structure as an array of
	 * <tt>BootstrapMethodsEntry</tt> structure.
	 *
	 * @return the array
	 */
	public BootstrapMethodsEntry[] methods() {
		return methods;
	}

	/**
	 * Set the list of bootstrap method references in the
	 * <tt>BootstrapMethodsAttribute</tt> structure as an array of
	 * <tt>BootstrapMethodsEntry</tt> structure.
	 *
	 * @param methods
	 *            the array
	 */
	public void setMethods(BootstrapMethodsEntry[] methods) {
		this.methods = methods;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		int numberOfRefs = in.readUnsignedShort();
		methods = new BootstrapMethodsEntry[numberOfRefs];
		for (int i = 0; i < numberOfRefs; i++)
			methods[i] = BootstrapMethodsEntry.create(in, classInfo);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		int numberOfRefs = length(methods);
		out.writeShort(numberOfRefs);
		for (int i = 0; i < numberOfRefs; i++)
			methods[i].write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	public int length() {
		int size = INITIAL_LENGTH;
		for (BootstrapMethodsEntry method : methods)
			size += method.length();
		return size;
	}

	@Override
	protected void debug(String message) {
		super.debug(message + "BootstrapMethods attribute with " + length(methods) + " references");
	}
}
