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
import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.constants.ConstantMethodHandleInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class MultianewarrayInstruction extends ImmediateShortInstruction {

	private int dimensions;

	/**
	 * Constructor.
	 *
	 * @param methodInfo
	 *            the MethodInfo that this instruction is within.
	 * @param opcode
	 *            the opcode.
	 */
	public MultianewarrayInstruction(MethodInfo methodInfo, Opcode opcode) {
		super(methodInfo, opcode);
	}

	@Override
	public int size() {
		return super.size() + 1;
	}

	/**
	 * Get the number of dimensions for the new array.
	 *
	 * @return the number of dimensions
	 */
	public int dimensions() {
		return dimensions;
	}

	/**
	 * Set the number of dimensions for the new array.
	 *
	 * @param dimensions
	 *            the number of dimensions
	 */
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	@Override
	public void read(BytecodeInput in) throws IOException {
		super.read(in);
		dimensions = in.readUnsignedByte();
	}

	@Override
	public void write(BytecodeOutput out) throws IOException {
		super.write(out);
		out.writeByte(dimensions);
	}

	private ConstantMethodHandleInfo info() {
		try {
			return (ConstantMethodHandleInfo) classInfo().constantPoolAt(value(), ConstantMethodHandleInfo.class);
		} catch (InvalidByteCodeException e) {
			return null;
		}
	}

	public String descriptor() {
		try {
			return info().name();
		} catch (NullPointerException | InvalidByteCodeException e) {
			return null;
		}
	}
}
