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
public class PaddedInstruction extends AbstractInstruction {

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public PaddedInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	/**
	 * Get the padded size in bits of this instruction.
	 *
	 * @param offset
	 *            the offset at which this instruction is found.
	 * @return the padded size in bits
	 */
	public int padding(int offset) {
		return size() + paddingBytes(offset + 1);
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		int bytesToRead = paddingBytes(in.count());
		for (int i = 0; i < bytesToRead; i++)
			in.readByte();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		int bytesToWrite = paddingBytes(out.count());
		for (int i = 0; i < bytesToWrite; i++)
			out.writeByte(0);
	}

	private int paddingBytes(int bytesCount) {
		int bytesToPad = 4 - bytesCount % 4;
		return (bytesToPad == 4) ? 0 : bytesToPad;
	}
}
