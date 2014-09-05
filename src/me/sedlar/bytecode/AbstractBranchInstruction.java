/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

import me.sedlar.bytecode.structure.MethodInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public abstract class AbstractBranchInstruction extends AbstractInstruction {

	private int branchOffset;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	protected AbstractBranchInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 * @param branchOffset
	 *            the branch offset.
	 */
	protected AbstractBranchInstruction(MethodInfo methodInfo, Opcode opcode, int branchOffset) {
		super(methodInfo, opcode);
		this.branchOffset = branchOffset;
	}

	/**
	 * Get the relative offset of the branch of this instruction.
	 *
	 * @return the relative offset
	 */
	public int branchOffset() {
		return branchOffset;
	}

	/**
	 * Get the offset of the branch of this instruction.
	 *
	 * @return the offset
	 */
	public int totalOffset() {
		return branchOffset() + offset();
	}

	/**
	 * Set the relative offset of the branch of this instruction.
	 *
	 * @param branchOffset
	 *            the offset
	 */
	public void setBranchOffset(int branchOffset) {
		this.branchOffset = branchOffset;
	}
}
