/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.sedlar.bytecode.io.BytecodeInput;
import me.sedlar.bytecode.io.BytecodeOutput;
import me.sedlar.bytecode.structure.MethodInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class LookupSwitchInstruction extends PaddedInstruction {

	private int defaultOffset;
	private List<OffsetPair> offsetPairs = new ArrayList<OffsetPair>();

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public LookupSwitchInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	@Override
	public int size() {
		return super.size() + 8 + 8 * offsetPairs.size();
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
	 * Get the match-offset pairs of the branch of this instruction as a
	 * <tt>java.util.List</tt> of <tt>OffsetPair</tt> elements.
	 *
	 * @return the list
	 */
	public List<OffsetPair> offsetPairs() {
		return offsetPairs;
	}

	/**
	 * Set the match-offset pairs of the branch of this instruction as a
	 * <tt>java.util.List</tt> of <tt>LookupSwitchInstruction.OffsetPair</tt>
	 * elements.
	 *
	 * @param offsetPairs
	 *            the list
	 */
	public void setOffsetPairs(List<OffsetPair> offsetPairs) {
		this.offsetPairs = offsetPairs;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		offsetPairs.clear();
		defaultOffset = in.readInt();
		int numberOfPairs = in.readInt();
		int match, offset;
		for (int i = 0; i < numberOfPairs; i++) {
			match = in.readInt();
			offset = in.readInt();
			offsetPairs.add(new OffsetPair(match, offset));
		}
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeInt(defaultOffset);
		int numberOfPairs = offsetPairs.size();
		out.writeInt(numberOfPairs);
		OffsetPair currentOffsetPair;
		for (OffsetPair offsetPair : offsetPairs) {
			currentOffsetPair = offsetPair;
			out.writeInt(currentOffsetPair.match());
			out.writeInt(currentOffsetPair.offset());
		}
	}
}
