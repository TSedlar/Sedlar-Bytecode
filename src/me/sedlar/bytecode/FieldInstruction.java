/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.constants.ConstantFieldrefInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class FieldInstruction extends ImmediateShortInstruction {

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public FieldInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 * @param immediateShort
	 *            the immediate short variable.
	 */
	public FieldInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort) {
		super(methodInfo, opcode, immediateShort);
	}

	private ConstantFieldrefInfo info() {
		try {
			return (ConstantFieldrefInfo) classInfo().constantPoolAt(value(), ConstantFieldrefInfo.class);
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Get the owner of the field invoke
	 *
	 * @return the owner of the field invoke
	 */
	public String owner() {
		try {
			return info().constantClassInfo().name();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Set the owner of the field invoke
	 *
	 * @param owner the new owner of the field invoke
	 */
	public void setOwner(String owner) {
		try {
			info().constantClassInfo().setName(owner);
		} catch (InvalidByteCodeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the physical field name being invoked
	 *
	 * @return the physical field name being invoked
	 */
	public String name() {
		try {
			return info().nameAndTypeInfo().name();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Set the physical field name being invoked
	 *
	 * @param name the physical field name being invoked
	 */
	public void setName(String name) {
		try {
			info().nameAndTypeInfo().setName(name);
		} catch (InvalidByteCodeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the physical field name being invoked
	 *
	 * @return the physical field name being invoked
	 */
	public String descriptor() {
		try {
			return info().nameAndTypeInfo().descriptor();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Set the physical field descriptor being invoked
	 */
	public void setDescriptor(String descriptor) {
		try {
			info().nameAndTypeInfo().setDescriptor(descriptor);
		} catch (InvalidByteCodeException e) {
			e.printStackTrace();
		}
	}
}
