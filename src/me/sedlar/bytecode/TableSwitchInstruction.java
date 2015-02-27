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
public class TableSwitchInstruction extends PaddedInstruction {

	private int defaultOffset;
	private int lowByte;
	private int highByte;
	private int[] jumpOffsets;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public TableSwitchInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	@Override
	public int size() {
		return super.size() + 12 + 4 * jumpOffsets.length;
	}

	/**
	 * Get the default offset of the branch of this instruction.
	 *
	 * @return the offset
	 */
	public int defaultOffset() {
		return defaultOffset;
	}

	/**
	 * Set the default offset of the branch of this instruction.
	 *
	 * @param defaultOffset
	 *            the offset
	 */
	public void setDefaultOffset(int defaultOffset) {
		this.defaultOffset = defaultOffset;
	}

	/**
	 * Get the lower bound for the table switch.
	 *
	 * @return the lower bound
	 */
	public int low() {
		return lowByte;
	}

	/**
	 * Set the lower bound for the table switch.
	 *
	 * @param lowByte
	 *            the lower bound
	 */
	public void setLow(int lowByte) {
		this.lowByte = lowByte;
	}

	/**
	 * Get the upper bound for the table switch.
	 *
	 * @return the upper bound
	 */
	public int high() {
		return highByte;
	}

	/**
	 * Set the upper bound for the table switch.
	 *
	 * @param highByte
	 *            the upper bound
	 */
	public void setHigh(int highByte) {
		this.highByte = highByte;
	}

	/**
	 * Get the array of relative jump offsets for the table switch.
	 *
	 * @return the array
	 */
	public int[] jumpOffsets() {
		return jumpOffsets;
	}

	/**
	 * Set the array of relative jump offsets for the table switch.
	 *
	 * @param jumpOffsets
	 *            the array
	 */
	public void setJumpOffsets(int[] jumpOffsets) {
		this.jumpOffsets = jumpOffsets;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		defaultOffset = in.readInt();
		lowByte = in.readInt();
		highByte = in.readInt();
		int numberOfOffsets = highByte - lowByte + 1;
		jumpOffsets = new int[numberOfOffsets];
		for (int i = 0; i < numberOfOffsets; i++)
			jumpOffsets[i] = in.readInt();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeInt(defaultOffset);
		out.writeInt(lowByte);
		out.writeInt(highByte);
		for (int jumpOffset : jumpOffsets)
			out.writeInt(jumpOffset);
	}
}
