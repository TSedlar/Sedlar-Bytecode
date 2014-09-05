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
public class ImmediateByteInstruction extends AbstractInstruction {

	/**
	 * Indicates whether the instruction is subject to a wide instruction or
	 * not.
	 */
	protected boolean wide;

	private int immediateByte;

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
	public ImmediateByteInstruction(MethodInfo methodInfo, Opcode opcode, boolean wide) {
		super(methodInfo, opcode);
		this.wide = wide;
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
	 */
	public ImmediateByteInstruction(MethodInfo methodInfo, Opcode opcode, boolean wide, int immediateByte) {
		this(methodInfo, opcode, wide);
		this.immediateByte = immediateByte;
	}

	@Override
	public int size() {
		return super.size() + (wide ? 2 : 1);
	}

	/**
	 * Get the immediate unsigned byte of this instruction.
	 *
	 * @return the byte
	 */
	public int value() {
		return immediateByte;
	}

	/**
	 * Set the immediate unsigned byte of this instruction.
	 *
	 * @param immediateByte
	 *            the byte
	 */
	public void setValue(int immediateByte) {
		this.immediateByte = immediateByte;
	}

	/**
	 * Check whether the instruction is subject to a wide instruction or not.
	 *
	 * @return wide or not
	 */
	public boolean wide() {
		return wide;
	}

	/**
	 * Set whether the instruction is subject to a wide instruction or not.
	 *
	 * @param wide
	 *            wide or not
	 */
	public void setWide(boolean wide) {
		this.wide = wide;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		immediateByte = (wide ? in.readShort() : in.readByte());
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		if (wide) {
			out.writeShort(immediateByte);
		} else {
			out.writeByte(immediateByte);
		}
	}
}
