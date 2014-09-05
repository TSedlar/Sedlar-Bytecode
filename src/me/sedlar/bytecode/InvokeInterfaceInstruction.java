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
public class InvokeInterfaceInstruction extends ImmediateShortInstruction {

	private int count;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public InvokeInterfaceInstruction(MethodInfo methodInfo, Opcode opcode) {
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
	 * @param count
	 *            the argument count.
	 */
	public InvokeInterfaceInstruction(MethodInfo methodInfo, Opcode opcode, int immediateShort, int count) {
		super(methodInfo, opcode, immediateShort);
		this.count = count;
	}

	@Override
	public int size() {
		return super.size() + 2;
	}

	/**
	 * Get the argument count of this instruction.
	 *
	 * @return the argument count
	 */
	public int count() {
		return count;
	}

	/**
	 * Set the argument count of this instruction.
	 *
	 * @param count
	 *            the argument count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		count = in.readUnsignedByte();
		// Next byte is always 0 and thus discarded
		in.readByte();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeByte(count);
		out.writeByte(0);
	}
}
