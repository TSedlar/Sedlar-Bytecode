/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import java.io.IOException;

import me.sedlar.bytecode.io.BytecodeInput;
import me.sedlar.bytecode.io.BytecodeOutput;
import me.sedlar.bytecode.structure.MethodInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class InvokeDynamicInstruction extends ImmediateShortInstruction {

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public InvokeDynamicInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode
	 * @param immediateShort
	 *            the immediate short variable.
	 */
	public InvokeDynamicInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort) {
		super(methodInfo, opcode, immediateShort);
	}

	@Override
	public int size() {
		return super.size() + 2;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		// Next two bits are always 0 and thus discarded
		in.readUnsignedShort();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeShort(0);
	}
}
