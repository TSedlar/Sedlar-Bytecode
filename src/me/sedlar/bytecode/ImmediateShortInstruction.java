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
public class ImmediateShortInstruction extends AbstractInstruction {

	private int immediateShort;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public ImmediateShortInstruction(MethodInfo methodInfo, Opcode opcode) {
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
	public ImmediateShortInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort) {
		super(methodInfo, opcode);
		this.immediateShort = immediateShort;
	}

	@Override
	public int size() {
		return super.size() + 2;
	}

	/**
	 * Get the immediate unsigned short of this instruction.
	 *
	 * @return the short
	 */
	public int value() {
		return immediateShort;
	}

	/**
	 * Set the immediate unsigned short of this instruction.
	 *
	 * @param immediateShort
	 *            the short
	 */
	public void setValue(int immediateShort) {
		this.immediateShort = immediateShort;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		immediateShort = in.readShort();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeShort(immediateShort);
	}
}
