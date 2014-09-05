/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.constants.ConstantClassInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class TypeInstruction extends ImmediateShortInstruction {

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public TypeInstruction(MethodInfo methodInfo, Opcode opcode) {
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
	public TypeInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort) {
		super(methodInfo, opcode, immediateShort);
	}

	public String type() {
		try {
			return ((ConstantClassInfo) classInfo().constantPoolAt(value(), ConstantClassInfo.class)).name();
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}
}
