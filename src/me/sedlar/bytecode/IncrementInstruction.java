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
public class IncrementInstruction extends ImmediateByteInstruction {

	private int incrementConst;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode
	 * @param wide
	 *            whether the instruction is a wide instruction.
	 */
	public IncrementInstruction(MethodInfo methodInfo, Opcode opcode, boolean wide) {
		super(methodInfo, opcode, wide);
	}

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode
	 * @param wide
	 *            whether the instruction is a wide instruction.
	 * @param immediateByte
	 *            the immediate byte variable.
	 * @param incrementConst
	 *            the increment.
	 */
	public IncrementInstruction(MethodInfo methodInfo, Opcode opcode, boolean wide, int immediateByte,
			int incrementConst) {
		super(methodInfo, opcode, wide, immediateByte);
		this.incrementConst = incrementConst;
	}

	@Override
	public int size() {
		return super.size() + (wide ? 2 : 1);
	}

	/**
	 * Get the increment of this instruction.
	 *
	 * @return the increment
	 */
	public int increment() {
		return incrementConst;
	}

	/**
	 * Set the increment of this instruction.
	 *
	 * @param incrementConst
	 *            the increment
	 */
	public void setIncrement(int incrementConst) {
		this.incrementConst = incrementConst;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		incrementConst = (wide ? in.readShort() : in.readByte());
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		if (wide) {
			out.writeShort(incrementConst);
		} else {
			out.writeByte(incrementConst);
		}
	}
}
