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

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class PushInstruction extends AbstractInstruction {

	private AbstractInstruction instruction;

	/**
	 * Constructor.
	 *
	 * @param ibi
	 *            an ImmediateByteInstruction for namesake an LDC* that pushes a
	 *            byte.
	 */
	public PushInstruction(ImmediateByteInstruction ibi) {
		super(ibi.methodInfo(), ibi.opcode());
		this.instruction = ibi;
	}

	/**
	 * Constructor.
	 *
	 * @param isi
	 *            an ImmediateShortInstruction for namesake an LDC* that pushes
	 *            a short
	 */
	public PushInstruction(ImmediateShortInstruction isi) {
		super(isi.methodInfo(), isi.opcode());
		this.instruction = isi;
	}

	/**
	 * Get the variable being pushed
	 *
	 * @return the variable being pushed
	 */
	public int value() {
		if (instruction instanceof ImmediateByteInstruction) {
			return ((ImmediateByteInstruction) instruction).value();
		} else {
			return ((ImmediateShortInstruction) instruction).value();
		}
	}

	/**
	 * Set the instruction's value.
	 *
	 * @param value
	 *            the value to set to
	 */
	public void setValue(int value) {
		if (instruction instanceof ImmediateByteInstruction) {
			((ImmediateByteInstruction) instruction).setValue(value);
		} else {
			((ImmediateShortInstruction) instruction).setValue(value);
		}
	}

	@Override
	public void read(BytecodeInput input) throws IOException {
		instruction.read(input);
	}

	@Override
	public void write(BytecodeOutput output) throws IOException {
		instruction.write(output);
	}
}
