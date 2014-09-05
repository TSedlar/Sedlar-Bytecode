/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.constants.ConstantMethodrefInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class MethodInstruction extends ImmediateShortInstruction {

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public MethodInstruction(MethodInfo methodInfo, Opcode opcode) {
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
	public MethodInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort) {
		super(methodInfo, opcode, immediateShort);
	}

	private ConstantMethodrefInfo info() {
		try {
			return (ConstantMethodrefInfo) classInfo().constantPoolAt(value(), ConstantMethodrefInfo.class);
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Get the owner of the invoke instruction
	 *
	 * @return the owner of the invoke instruction
	 */
	public String owner() {
		try {
			return info().constantClassInfo().name();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Get the physical method name being invoked
	 *
	 * @return the physical method name being invoked
	 */
	public String name() {
		try {
			return info().nameAndTypeInfo().name();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	/**
	 * Get the physical method descriptor being invoked
	 *
	 * @return the physical method descriptor being invoked
	 */
	public String descriptor() {
		try {
			return info().nameAndTypeInfo().descriptor();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}
}
